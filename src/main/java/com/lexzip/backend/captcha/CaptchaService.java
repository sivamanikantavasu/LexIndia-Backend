package com.lexzip.backend.captcha;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CaptchaService {

    private final CaptchaCodeRepository captchaCodeRepository;

    public CaptchaService(CaptchaCodeRepository captchaCodeRepository) {
        this.captchaCodeRepository = captchaCodeRepository;
    }

    @Transactional(readOnly = true)
    public CaptchaResponse getRandomCaptcha() {
        List<CaptchaCode> captchaCodes = captchaCodeRepository.findAll();
        if (captchaCodes.isEmpty()) {
            throw new IllegalArgumentException("No captcha codes available");
        }
        String code = captchaCodes.get(ThreadLocalRandom.current().nextInt(captchaCodes.size())).getCode();
        return new CaptchaResponse(code);
    }

    public CaptchaValidationResponse validate(CaptchaValidationRequest request) {
        return new CaptchaValidationResponse(request.userInput().trim().equals(request.correctCaptcha()));
    }
}
