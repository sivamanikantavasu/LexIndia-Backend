package com.lexzip.backend.insight;

import com.lexzip.backend.auth.AuthService;
import com.lexzip.backend.auth.Profile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/legal-insights")
public class LegalInsightController {

    private final LegalInsightRepository legalInsightRepository;
    private final AuthService authService;

    public LegalInsightController(LegalInsightRepository legalInsightRepository, AuthService authService) {
        this.legalInsightRepository = legalInsightRepository;
        this.authService = authService;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<LegalInsightResponse> getInsights(@RequestParam(required = false) String scope) {
        List<LegalInsight> insights = "mine".equalsIgnoreCase(scope)
                ? legalInsightRepository.findAllByOrderByCreatedAtDesc()
                : legalInsightRepository.findAllByOrderByCreatedAtDesc();
        return insights.stream().map(LegalInsightResponse::from).toList();
    }

    @PostMapping
    @Transactional
    public LegalInsightResponse createInsight(
            @RequestHeader(name = "X-Session-Token", required = false) String token,
            @RequestBody LegalInsightRequest request
    ) {
        Profile profile = authService.requireAuthenticatedProfile(token);
        LegalInsight insight = new LegalInsight();
        insight.setExpert(profile);
        insight.setTitle(request.title().trim());
        insight.setCategory(request.category().trim());
        insight.setContent(request.content().trim());
        insight.setTags(parseTags(request.tags()));
        insight.setStatus(blankToDefault(request.status(), "published"));
        insight.setViews(0);
        insight.setShares(0);
        return LegalInsightResponse.from(legalInsightRepository.save(insight));
    }

    private List<String> parseTags(String tags) {
        if (tags == null || tags.isBlank()) {
            return List.of();
        }
        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .distinct()
                .toList();
    }

    private String blankToDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim().toLowerCase();
    }
}
