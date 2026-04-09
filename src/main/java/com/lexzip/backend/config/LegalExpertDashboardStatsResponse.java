package com.lexzip.backend.config;

public record LegalExpertDashboardStatsResponse(
        long pendingAdvisoryRequests,
        long articlesCount,
        long insightsCount,
        long caseReferencesCount
) {
}
