package com.lexzip.backend.config;

import com.lexzip.backend.advisory.AdvisoryRequestRepository;
import com.lexzip.backend.advisory.AdvisoryStatus;
import com.lexzip.backend.article.ArticleRepository;
import com.lexzip.backend.insight.CaseReferenceRepository;
import com.lexzip.backend.insight.LegalInsightRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final AdvisoryRequestRepository advisoryRequestRepository;
    private final ArticleRepository articleRepository;
    private final LegalInsightRepository legalInsightRepository;
    private final CaseReferenceRepository caseReferenceRepository;

    public DashboardController(
            AdvisoryRequestRepository advisoryRequestRepository,
            ArticleRepository articleRepository,
            LegalInsightRepository legalInsightRepository,
            CaseReferenceRepository caseReferenceRepository
    ) {
        this.advisoryRequestRepository = advisoryRequestRepository;
        this.articleRepository = articleRepository;
        this.legalInsightRepository = legalInsightRepository;
        this.caseReferenceRepository = caseReferenceRepository;
    }

    @GetMapping("/legal-expert/stats")
    public LegalExpertDashboardStatsResponse getLegalExpertStats() {
        return new LegalExpertDashboardStatsResponse(
                advisoryRequestRepository.countByStatus(AdvisoryStatus.PENDING),
                articleRepository.count(),
                legalInsightRepository.count(),
                caseReferenceRepository.count()
        );
    }
}
