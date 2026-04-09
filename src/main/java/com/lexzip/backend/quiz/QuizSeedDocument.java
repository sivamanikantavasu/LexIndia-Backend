package com.lexzip.backend.quiz;

import java.util.List;

public record QuizSeedDocument(
        Long id,
        String title,
        String description,
        List<QuizSeedQuestion> questions
) {
    public record QuizSeedQuestion(
            Long id,
            String question,
            List<String> options,
            Integer correctAnswer,
            String explanation
    ) {
    }
}
