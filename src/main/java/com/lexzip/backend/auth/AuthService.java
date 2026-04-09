package com.lexzip.backend.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final ProfileRepository profileRepository;
    private final AppUserRepository appUserRepository;
    private final AuthSessionRepository authSessionRepository;
    private final AuthProperties authProperties;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(
            ProfileRepository profileRepository,
            AppUserRepository appUserRepository,
            AuthSessionRepository authSessionRepository,
            AuthProperties authProperties
    ) {
        this.profileRepository = profileRepository;
        this.appUserRepository = appUserRepository;
        this.authSessionRepository = authSessionRepository;
        this.authProperties = authProperties;
    }

    @Transactional
    public AuthResponse signUp(SignUpRequest request) {
        profileRepository.findByEmailIgnoreCase(request.email()).ifPresent(existing -> {
            throw new IllegalArgumentException("Email is already registered");
        });

        Profile profile = new Profile();
        profile.setId(UUID.randomUUID());
        profile.setEmail(request.email().trim().toLowerCase());
        profile.setFullName(request.fullName().trim());
        profile.setRole(Role.fromValue(request.role()));
        profileRepository.save(profile);

        AppUser user = new AppUser();
        user.setProfile(profile);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        appUserRepository.save(user);

        return createSession(profile);
    }

    @Transactional
    public AuthResponse signIn(SignInRequest request) {
        AppUser user = appUserRepository.findByProfileEmailIgnoreCase(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return createSession(user.getProfile());
    }

    @Transactional
    public void signOut(String token) {
        if (token == null || token.isBlank()) {
            return;
        }
        authSessionRepository.deleteById(token);
    }

    @Transactional(readOnly = true)
    public SessionResponse getSession(String token) {
        if (token == null || token.isBlank()) {
            return new SessionResponse(false, null);
        }

        return authSessionRepository.findById(token)
                .filter(session -> session.getExpiresAt().isAfter(OffsetDateTime.now()))
                .map(session -> new SessionResponse(true, toAuthResponse(session)))
                .orElseGet(() -> new SessionResponse(false, null));
    }

    @Transactional(readOnly = true)
    public ProfileResponse getUserProfile(UUID userId) {
        return profileRepository.findById(userId)
                .map(ProfileResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
    }

    @Transactional(readOnly = true)
    public Profile requireAuthenticatedProfile(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Authentication required");
        }

        AuthSession session = authSessionRepository.findById(token)
                .filter(existing -> existing.getExpiresAt().isAfter(OffsetDateTime.now()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired session"));

        return session.getUser();
    }

    @Transactional
    protected AuthResponse createSession(Profile profile) {
        authSessionRepository.deleteByExpiresAtBefore(OffsetDateTime.now());

        AuthSession session = new AuthSession();
        session.setToken(UUID.randomUUID().toString());
        session.setUser(profile);
        session.setExpiresAt(OffsetDateTime.now().plusHours(authProperties.getSessionTtlHours()));
        authSessionRepository.save(session);
        return toAuthResponse(session);
    }

    private AuthResponse toAuthResponse(AuthSession session) {
        return new AuthResponse(
                session.getToken(),
                session.getExpiresAt(),
                ProfileResponse.from(session.getUser())
        );
    }
}
