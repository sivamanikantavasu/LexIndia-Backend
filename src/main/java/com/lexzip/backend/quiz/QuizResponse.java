package com.lexzip.backend.quiz;

import java.util.List;

public record QuizResponse(
        Long id,
        String title,
        String description,
        List<QuizQuestionResponse> questions
) {
}
