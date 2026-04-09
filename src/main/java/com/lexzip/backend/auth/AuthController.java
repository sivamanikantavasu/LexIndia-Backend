package com.lexzip.backend.auth;

import jakarta.validation.Valid;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public AuthController(AuthService authService, ObjectMapper objectMapper, Validator validator) {
        this.authService = authService;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @PostMapping(value = "/signup", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public AuthResponse signUp(@RequestBody String requestBody) {
        SignUpRequest request = parseAndValidate(requestBody, SignUpRequest.class);
        return authService.signUp(request);
    }

    @PostMapping(value = "/signin", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public AuthResponse signIn(@RequestBody String requestBody) {
        SignInRequest request = parseAndValidate(requestBody, SignInRequest.class);
        return authService.signIn(request);
    }

    @PostMapping("/signout")
    public ResponseEntity<Map<String, Object>> signOut(
            @RequestHeader(name = "X-Session-Token", required = false) String token
    ) {
        authService.signOut(token);
        return ResponseEntity.ok(Map.of("signedOut", true));
    }

    @GetMapping("/session")
    public SessionResponse getSession(
            @RequestHeader(name = "X-Session-Token", required = false) String token
    ) {
        return authService.getSession(token);
    }

    @GetMapping("/profiles/{userId}")
    public ProfileResponse getUserProfile(@PathVariable UUID userId) {
        return authService.getUserProfile(userId);
    }

    private <T> T parseAndValidate(String requestBody, Class<T> type) {
        try {
            T request = objectMapper.readValue(requestBody, type);
            var violations = validator.validate(request);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException((java.util.Set<ConstraintViolation<?>>) (java.util.Set<?>) violations);
            }
            return request;
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Invalid request payload", exception);
        }
    }
}
