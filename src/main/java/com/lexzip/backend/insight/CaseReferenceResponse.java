package com.lexzip.backend.insight;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record CaseReferenceResponse(
        UUID id,
        String title,
        Integer year,
        String citation,
        String court,
        String type,
        String significance,
        String summary,
        List<String> relevantArticles,
        String judgeName,
        String pdfUrl,
        String status,
        OffsetDateTime createdAt
) {
    static CaseReferenceResponse from(CaseReference reference) {
        return new CaseReferenceResponse(
                reference.getId(),
                reference.getTitle(),
                reference.getYear(),
                reference.getCitation(),
                reference.getCourt(),
                reference.getType(),
                reference.getSignificance(),
                reference.getSummary(),
                reference.getRelevantArticles() == null ? List.of() : reference.getRelevantArticles(),
                reference.getJudgeName(),
                reference.getPdfUrl(),
                reference.getStatus(),
                reference.getCreatedAt()
        );
    }
}
