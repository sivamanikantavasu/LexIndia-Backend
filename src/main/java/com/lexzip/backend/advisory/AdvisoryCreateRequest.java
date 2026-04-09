package com.lexzip.backend.advisory;

public record AdvisoryCreateRequest(
        String subject,
        String question,
        String category,
        boolean urgent
) {
}
