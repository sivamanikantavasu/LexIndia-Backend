package com.lexzip.backend.auth;

import java.time.OffsetDateTime;

public record AuthResponse(
        String token,
        OffsetDateTime expiresAt,
        ProfileResponse profile
) {
}
