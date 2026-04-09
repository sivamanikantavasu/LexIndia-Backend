package com.lexzip.backend.captcha;

import jakarta.validation.constraints.NotBlank;

public record CaptchaValidationRequest(
        @NotBlank String userInput,
        @NotBlank String correctCaptcha
) {
}
