package com.lexzip.backend.insight;

import com.lexzip.backend.auth.AuthService;
import com.lexzip.backend.auth.Profile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/case-references")
public class CaseReferenceController {

    private final CaseReferenceRepository caseReferenceRepository;
    private final AuthService authService;

    public CaseReferenceController(CaseReferenceRepository caseReferenceRepository, AuthService authService) {
        this.caseReferenceRepository = caseReferenceRepository;
        this.authService = authService;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<CaseReferenceResponse> getCaseReferences() {
        return caseReferenceRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(CaseReferenceResponse::from)
                .toList();
    }

    @PostMapping
    @Transactional
    public CaseReferenceResponse createCaseReference(
            @RequestHeader(name = "X-Session-Token", required = false) String token,
            @RequestBody CaseReferenceRequest request
    ) {
        Profile profile = authService.requireAuthenticatedProfile(token);
        if (profile == null) {
            throw new IllegalArgumentException("Authentication required");
        }

        CaseReference reference = new CaseReference();
        reference.setTitle(request.title().trim());
        reference.setCitation(request.citation());
        reference.setSummary(request.summary());
        reference.setType(request.type());
        reference.setCourt(request.court());
        reference.setYear(request.year());
        reference.setSignificance(request.significance());
        reference.setRelevantArticles(parseArticles(request.relevantArticles()));
        reference.setJudgeName(request.judgeName());
        reference.setPdfUrl(request.pdfUrl());
        reference.setStatus(blankToDefault(request.status(), "published"));
        return CaseReferenceResponse.from(caseReferenceRepository.save(reference));
    }

    private List<String> parseArticles(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(entry -> !entry.isBlank())
                .distinct()
                .toList();
    }

    private String blankToDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim().toLowerCase();
    }
}
