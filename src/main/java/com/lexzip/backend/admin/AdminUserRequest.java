package com.lexzip.backend.admin;

public record AdminUserRequest(
        String name,
        String email,
        String role,
        String password
) {
}
