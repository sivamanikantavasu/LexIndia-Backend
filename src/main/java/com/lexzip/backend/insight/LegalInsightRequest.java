package com.lexzip.backend.insight;

public record LegalInsightRequest(
        String title,
        String category,
        String content,
        String tags,
        String status
) {
}
