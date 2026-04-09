package com.lexzip.backend.insight;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CaseReferenceRepository extends JpaRepository<CaseReference, UUID> {
    List<CaseReference> findAllByOrderByCreatedAtDesc();
}
