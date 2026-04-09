package com.lexzip.backend.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 6, max = 100) String password,
        @NotBlank @Size(max = 120) String fullName,
        String role
) {
}
