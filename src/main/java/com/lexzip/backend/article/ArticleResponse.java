package com.lexzip.backend.article;

import java.util.List;
import java.util.UUID;

public record ArticleResponse(
        UUID id,
        String category,
        Integer number,
        String title,
        String fullText,
        String explanation,
        List<String> keyPoints,
        List<String> importantCases
) {
    public static ArticleResponse from(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getCategory(),
                article.getNumber(),
                article.getTitle(),
                article.getFullText(),
                article.getExplanation(),
                article.getKeyPoints(),
                article.getImportantCases()
        );
    }
}
