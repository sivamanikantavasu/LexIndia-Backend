package com.lexzip.backend.quiz;

import java.util.List;

public record QuizQuestionResponse(
        Long id,
        String question,
        List<String> options,
        Integer correctAnswer,
        String explanation
) {
    public static QuizQuestionResponse from(QuizQuestion question) {
        return new QuizQuestionResponse(
                question.getId(),
                question.getQuestion(),
                question.getOptions(),
                question.getCorrectAnswer(),
                question.getExplanation()
        );
    }
}
