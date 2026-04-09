package com.lexzip.backend.insight;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LegalInsightRepository extends JpaRepository<LegalInsight, UUID> {
    List<LegalInsight> findAllByOrderByCreatedAtDesc();

    List<LegalInsight> findByExpertIdOrderByCreatedAtDesc(UUID expertId);
}
