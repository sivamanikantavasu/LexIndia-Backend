package com.lexzip.backend.admin;

import com.lexzip.backend.auth.Profile;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AdminUserResponse(
        UUID id,
        String name,
        String email,
        String role,
        String status,
        String phone,
        String location,
        OffsetDateTime joinedDate,
        OffsetDateTime lastActive
) {
    static AdminUserResponse from(Profile profile) {
        return new AdminUserResponse(
                profile.getId(),
                profile.getFullName(),
                profile.getEmail(),
                toLabel(profile.getRole().getValue()),
                "Active",
                "",
                "",
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }

    private static String toLabel(String role) {
        return switch (role) {
            case "legal-expert" -> "Legal Expert";
            case "educator" -> "Educator";
            case "admin" -> "Administrator";
            default -> "Citizen";
        };
    }
}
