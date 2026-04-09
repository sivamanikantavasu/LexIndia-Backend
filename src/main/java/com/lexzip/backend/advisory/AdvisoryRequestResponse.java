package com.lexzip.backend.advisory;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AdvisoryRequestResponse(
        UUID id,
        String subject,
        String question,
        String category,
        String status,
        boolean urgent,
        String response,
        OffsetDateTime date,
        String from,
        String citizenEmail,
        String expertName
) {
    static AdvisoryRequestResponse from(AdvisoryRequest request) {
        return new AdvisoryRequestResponse(
                request.getId(),
                request.getSubject() == null || request.getSubject().isBlank() ? request.getQuestion() : request.getSubject(),
                request.getQuestion(),
                request.getCategory(),
                request.getStatus().name().toLowerCase(),
                request.isUrgent(),
                request.getResponse(),
                request.getCreatedAt(),
                request.getCitizen() == null ? "Unknown Citizen" : request.getCitizen().getFullName(),
                request.getCitizen() == null ? null : request.getCitizen().getEmail(),
                request.getExpert() == null ? null : request.getExpert().getFullName()
        );
    }
}
