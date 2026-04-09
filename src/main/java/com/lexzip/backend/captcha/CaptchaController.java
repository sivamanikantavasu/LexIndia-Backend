package com.lexzip.backend.captcha;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    private final CaptchaService captchaService;

    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @GetMapping("/random")
    public CaptchaResponse getRandomCaptcha() {
        return captchaService.getRandomCaptcha();
    }

    @PostMapping("/validate")
    public CaptchaValidationResponse validate(@Valid @RequestBody CaptchaValidationRequest request) {
        return captchaService.validate(request);
    }
}
