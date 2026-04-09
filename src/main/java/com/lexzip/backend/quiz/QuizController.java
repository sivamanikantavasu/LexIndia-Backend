package com.lexzip.backend.quiz;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public List<QuizResponse> getQuizzes() {
        return quizService.getQuizzes();
    }

    @GetMapping("/{id}")
    public QuizResponse getQuiz(@PathVariable Long id) {
        return quizService.getQuiz(id);
    }

    @GetMapping("/random/set")
    public QuizResponse getRandomQuiz() {
        return quizService.getRandomQuiz();
    }
}
