package com.lexzip.backend.captcha;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CaptchaCodeRepository extends JpaRepository<CaptchaCode, Long> {
}
