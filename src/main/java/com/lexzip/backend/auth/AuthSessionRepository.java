package com.lexzip.backend.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;

public interface AuthSessionRepository extends JpaRepository<AuthSession, String> {
    void deleteByExpiresAtBefore(OffsetDateTime time);
}
