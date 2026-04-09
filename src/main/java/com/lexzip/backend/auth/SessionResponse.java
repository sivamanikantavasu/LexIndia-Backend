package com.lexzip.backend.auth;

public record SessionResponse(
        boolean authenticated,
        AuthResponse session
) {
}
