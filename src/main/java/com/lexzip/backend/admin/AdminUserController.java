package com.lexzip.backend.admin;

import com.lexzip.backend.auth.AppUser;
import com.lexzip.backend.auth.AppUserRepository;
import com.lexzip.backend.auth.Profile;
import com.lexzip.backend.auth.ProfileRepository;
import com.lexzip.backend.auth.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private final ProfileRepository profileRepository;
    private final AppUserRepository appUserRepository;

    public AdminUserController(ProfileRepository profileRepository, AppUserRepository appUserRepository) {
        this.profileRepository = profileRepository;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<AdminUserResponse> getUsers() {
        return profileRepository.findAll().stream()
                .sorted((left, right) -> right.getCreatedAt().compareTo(left.getCreatedAt()))
                .map(AdminUserResponse::from)
                .toList();
    }

    @PostMapping
    @Transactional
    public AdminUserResponse createUser(@RequestBody AdminUserRequest request) {
        Profile profile = new Profile();
        profile.setId(UUID.randomUUID());
        profile.setFullName(request.name());
        profile.setEmail(request.email().trim().toLowerCase());
        profile.setRole(Role.fromValue(request.role()));
        profile = profileRepository.save(profile);

        AppUser user = new AppUser();
        user.setProfile(profile);
        user.setPasswordHash(PASSWORD_ENCODER.encode(defaultPassword(request.password())));
        appUserRepository.save(user);

        return AdminUserResponse.from(profile);
    }

    @PutMapping("/{userId}")
    @Transactional
    public AdminUserResponse updateUser(@PathVariable UUID userId, @RequestBody AdminUserRequest request) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        profile.setFullName(request.name());
        profile.setEmail(request.email().trim().toLowerCase());
        profile.setRole(Role.fromValue(request.role()));
        profile.setUpdatedAt(OffsetDateTime.now());
        return AdminUserResponse.from(profileRepository.save(profile));
    }

    @DeleteMapping("/{userId}")
    @Transactional
    public void deleteUser(@PathVariable UUID userId) {
        profileRepository.deleteById(userId);
    }

    private String defaultPassword(String value) {
        return value == null || value.isBlank() ? "password@123" : value;
    }
}
