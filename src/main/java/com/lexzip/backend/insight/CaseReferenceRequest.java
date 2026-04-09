package com.lexzip.backend.insight;

public record CaseReferenceRequest(
        String title,
        Integer year,
        String citation,
        String court,
        String type,
        String significance,
        String summary,
        String relevantArticles,
        String judgeName,
        String pdfUrl,
        String status
) {
}
