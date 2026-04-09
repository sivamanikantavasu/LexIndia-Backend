package com.lexzip.backend.insight;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record LegalInsightResponse(
        UUID id,
        String title,
        String category,
        String content,
        List<String> tags,
        String status,
        long views,
        long shares,
        OffsetDateTime date,
        String expertName
) {
    static LegalInsightResponse from(LegalInsight insight) {
        return new LegalInsightResponse(
                insight.getId(),
                insight.getTitle(),
                insight.getCategory(),
                insight.getContent(),
                insight.getTags() == null ? List.of() : insight.getTags(),
                insight.getStatus(),
                insight.getViews(),
                insight.getShares(),
                insight.getCreatedAt(),
                insight.getExpert() == null ? "Unknown Expert" : insight.getExpert().getFullName()
        );
    }
}
