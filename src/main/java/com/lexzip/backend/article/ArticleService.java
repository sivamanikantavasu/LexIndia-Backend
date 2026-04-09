package com.lexzip.backend.article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleCategoryRepository articleCategoryRepository;

    public ArticleService(ArticleRepository articleRepository, ArticleCategoryRepository articleCategoryRepository) {
        this.articleRepository = articleRepository;
        this.articleCategoryRepository = articleCategoryRepository;
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getArticles(String category) {
        List<Article> articles = category == null || category.isBlank()
                ? articleRepository.findAllByOrderByCategoryAscNumberAsc()
                : articleRepository.findByCategoryOrderByNumberAsc(category);
        return articles.stream().map(ArticleResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ArticleResponse getArticle(String category, Integer number) {
        return articleRepository.findByCategoryAndNumber(category, number)
                .map(ArticleResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));
    }

    @Transactional(readOnly = true)
    public List<ArticleCategoryResponse> getCategories() {
        return articleCategoryRepository.findAllByOrderByTitleAsc().stream()
                .map(category -> new ArticleCategoryResponse(
                        category.getId(),
                        category.getTitle(),
                        category.getDescription()
                ))
                .toList();
    }
}
