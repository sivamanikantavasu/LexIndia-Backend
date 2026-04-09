package com.lexzip.backend.quiz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;

    public QuizService(QuizRepository quizRepository, QuizQuestionRepository quizQuestionRepository) {
        this.quizRepository = quizRepository;
        this.quizQuestionRepository = quizQuestionRepository;
    }

    @Transactional(readOnly = true)
    public List<QuizResponse> getQuizzes() {
        return quizRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public QuizResponse getQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));
        return toResponse(quiz);
    }

    @Transactional(readOnly = true)
    public QuizResponse getRandomQuiz() {
        List<Quiz> quizzes = quizRepository.findAll();
        if (quizzes.isEmpty()) {
            throw new IllegalArgumentException("No quizzes available");
        }
        Quiz quiz = quizzes.get(ThreadLocalRandom.current().nextInt(quizzes.size()));
        return toResponse(quiz);
    }

    private QuizResponse toResponse(Quiz quiz) {
        List<QuizQuestionResponse> questions = quizQuestionRepository.findByQuizIdOrderByIdAsc(quiz.getId()).stream()
                .map(QuizQuestionResponse::from)
                .toList();
        return new QuizResponse(quiz.getId(), quiz.getTitle(), quiz.getDescription(), questions);
    }
}
