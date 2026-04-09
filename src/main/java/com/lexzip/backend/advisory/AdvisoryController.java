package com.lexzip.backend.advisory;

import com.lexzip.backend.auth.AuthService;
import com.lexzip.backend.auth.Profile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/advisory")
public class AdvisoryController {

    private final AdvisoryRequestRepository advisoryRequestRepository;
    private final AuthService authService;

    public AdvisoryController(AdvisoryRequestRepository advisoryRequestRepository, AuthService authService) {
        this.advisoryRequestRepository = advisoryRequestRepository;
        this.authService = authService;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<AdvisoryRequestResponse> getRequests() {
        return advisoryRequestRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(AdvisoryRequestResponse::from)
                .toList();
    }

    @PostMapping
    @Transactional
    public AdvisoryRequestResponse createRequest(
            @RequestHeader(name = "X-Session-Token", required = false) String token,
            @RequestBody AdvisoryCreateRequest request
    ) {
        Profile profile = authService.requireAuthenticatedProfile(token);
        AdvisoryRequest advisoryRequest = new AdvisoryRequest();
        advisoryRequest.setCitizen(profile);
        advisoryRequest.setSubject(request.subject());
        advisoryRequest.setQuestion(request.question());
        advisoryRequest.setCategory(request.category());
        advisoryRequest.setUrgent(request.urgent());
        advisoryRequest.setStatus(AdvisoryStatus.PENDING);
        return AdvisoryRequestResponse.from(advisoryRequestRepository.save(advisoryRequest));
    }

    @PatchMapping("/{requestId}/response")
    @Transactional
    public AdvisoryRequestResponse respond(
            @PathVariable UUID requestId,
            @RequestHeader(name = "X-Session-Token", required = false) String token,
            @RequestBody AdvisoryRespondRequest request
    ) {
        Profile expert = authService.requireAuthenticatedProfile(token);
        AdvisoryRequest advisoryRequest = advisoryRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Advisory request not found"));
        advisoryRequest.setExpert(expert);
        advisoryRequest.setResponse(request.response().trim());
        advisoryRequest.setStatus(AdvisoryStatus.RESPONDED);
        advisoryRequest.setUpdatedAt(OffsetDateTime.now());
        return AdvisoryRequestResponse.from(advisoryRequestRepository.save(advisoryRequest));
    }
}
