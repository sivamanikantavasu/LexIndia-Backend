package com.lexzip.backend.advisory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AdvisoryRequestRepository extends JpaRepository<AdvisoryRequest, UUID> {
    long countByStatus(AdvisoryStatus status);

    List<AdvisoryRequest> findAllByOrderByCreatedAtDesc();
}
