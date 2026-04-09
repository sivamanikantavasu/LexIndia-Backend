package com.lexzip.backend.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lexzip.backend.article.ArticleCategory;
import com.lexzip.backend.article.ArticleCategoryRepository;
import com.lexzip.backend.article.Article;
import com.lexzip.backend.article.ArticleRepository;
import com.lexzip.backend.article.ArticleSeedDocument;
import com.lexzip.backend.auth.AppUser;
import com.lexzip.backend.auth.AppUserRepository;
import com.lexzip.backend.auth.Profile;
import com.lexzip.backend.auth.ProfileRepository;
import com.lexzip.backend.auth.Role;
import com.lexzip.backend.captcha.CaptchaCode;
import com.lexzip.backend.captcha.CaptchaCodeRepository;
import com.lexzip.backend.quiz.Quiz;
import com.lexzip.backend.quiz.QuizQuestion;
import com.lexzip.backend.quiz.QuizQuestionRepository;
import com.lexzip.backend.quiz.QuizRepository;
import com.lexzip.backend.quiz.QuizSeedDocument;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private final ArticleRepository articleRepository;
    private final ArticleCategoryRepository articleCategoryRepository;
    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final CaptchaCodeRepository captchaCodeRepository;
    private final ProfileRepository profileRepository;
    private final AppUserRepository appUserRepository;
    private final ObjectMapper objectMapper;

    public DataSeeder(
            ArticleRepository articleRepository,
            ArticleCategoryRepository articleCategoryRepository,
            QuizRepository quizRepository,
            QuizQuestionRepository quizQuestionRepository,
            CaptchaCodeRepository captchaCodeRepository,
            ProfileRepository profileRepository,
            AppUserRepository appUserRepository,
            ObjectMapper objectMapper
    ) {
        this.articleRepository = articleRepository;
        this.articleCategoryRepository = articleCategoryRepository;
        this.quizRepository = quizRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.captchaCodeRepository = captchaCodeRepository;
        this.profileRepository = profileRepository;
        this.appUserRepository = appUserRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedArticles();
        seedQuizzes();
        seedCaptchaCodes();
        seedDefaultUsers();
    }

    private void seedArticles() throws Exception {
        boolean hasArticles = articleRepository.count() > 0;
        boolean hasCategories = articleCategoryRepository.count() > 0;

        if (hasArticles && hasCategories) {
            return;
        }

        try (InputStream inputStream = new ClassPathResource("data/articles.json").getInputStream()) {
            Map<String, ArticleSeedDocument.ArticleSeedCategory> articleSeedData = objectMapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    }
            );

            for (Map.Entry<String, ArticleSeedDocument.ArticleSeedCategory> entry : articleSeedData.entrySet()) {
                if (!articleCategoryRepository.existsById(entry.getKey())) {
                    ArticleCategory category = new ArticleCategory();
                    category.setId(entry.getKey());
                    category.setTitle(entry.getValue().title());
                    category.setDescription(entry.getValue().description());
                    articleCategoryRepository.save(category);
                }

                if (hasArticles) {
                    continue;
                }

                for (ArticleSeedDocument.ArticleSeedItem item : entry.getValue().articles()) {
                    Article article = new Article();
                    article.setCategory(entry.getKey());
                    article.setNumber(item.number());
                    article.setTitle(item.title());
                    article.setFullText(item.fullText());
                    article.setExplanation(item.explanation());
                    article.setKeyPoints(item.keyPoints());
                    article.setImportantCases(item.importantCases());
                    articleRepository.save(article);
                }
            }
        }
    }

    private void seedQuizzes() throws Exception {
        if (quizRepository.count() > 0) {
            return;
        }

        try (InputStream inputStream = new ClassPathResource("data/quizzes.json").getInputStream()) {
            List<QuizSeedDocument> documents = objectMapper.readValue(inputStream, new TypeReference<>() {
            });

            for (QuizSeedDocument document : documents) {
                Quiz quiz = new Quiz();
                quiz.setId(document.id());
                quiz.setTitle(document.title());
                quiz.setDescription(document.description());
                quizRepository.save(quiz);

                for (QuizSeedDocument.QuizSeedQuestion sourceQuestion : document.questions()) {
                    QuizQuestion question = new QuizQuestion();
                    question.setQuiz(quiz);
                    question.setQuestion(sourceQuestion.question());
                    question.setOptions(sourceQuestion.options());
                    question.setCorrectAnswer(sourceQuestion.correctAnswer());
                    question.setExplanation(sourceQuestion.explanation());
                    quizQuestionRepository.save(question);
                }
            }
        }
    }

    private void seedCaptchaCodes() throws Exception {
        if (captchaCodeRepository.count() > 0) {
            return;
        }

        try (InputStream inputStream = new ClassPathResource("data/captcha-codes.json").getInputStream()) {
            List<String> codes = objectMapper.readValue(inputStream, new TypeReference<>() {
            });

            for (String value : codes) {
                CaptchaCode code = new CaptchaCode();
                code.setCode(value);
                captchaCodeRepository.save(code);
            }
        }
    }

    private void seedDefaultUsers() {
        seedUser("admin@lexindia.com", "System Administrator", Role.ADMIN, "admin@1234");
        seedUser("educator@lexindia.com", "Demo Educator", Role.EDUCATOR, "educator@1234");
        seedUser("legalexpert@lexindia.com", "Demo Legal Expert", Role.LEGAL_EXPERT, "legal@1234");
        seedUser("citizen@lexindia.com", "Demo Citizen", Role.CITIZEN, "citizen@1234");
    }

    private void seedUser(String email, String fullName, Role role, String rawPassword) {
        Profile profile = profileRepository.findByEmailIgnoreCase(email)
                .orElseGet(() -> {
                    Profile newProfile = new Profile();
                    newProfile.setId(UUID.randomUUID());
                    newProfile.setEmail(email);
                    newProfile.setFullName(fullName);
                    newProfile.setRole(role);
                    return profileRepository.save(newProfile);
                });

        if (appUserRepository.findById(profile.getId()).isPresent()) {
            return;
        }

        AppUser user = new AppUser();
        user.setProfile(profile);
        user.setPasswordHash(PASSWORD_ENCODER.encode(rawPassword));
        appUserRepository.save(user);
    }
}
