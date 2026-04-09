package com.lexzip.backend.auth;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ProfileResponse(
        UUID id,
        String email,
        String fullName,
        String role,
        String avatarUrl,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static ProfileResponse from(Profile profile) {
        return new ProfileResponse(
                profile.getId(),
                profile.getEmail(),
                profile.getFullName(),
                profile.getRole().getValue(),
                profile.getAvatarUrl(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }
}
