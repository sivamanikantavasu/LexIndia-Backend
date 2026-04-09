package com.lexzip.backend.article;

import java.util.List;
import java.util.Map;

public record ArticleSeedDocument(Map<String, ArticleSeedCategory> categories) {

    public record ArticleSeedCategory(
            String title,
            String description,
            List<ArticleSeedItem> articles
    ) {
    }

    public record ArticleSeedItem(
            Integer number,
            String title,
            String fullText,
            String explanation,
            List<String> keyPoints,
            List<String> importantCases
    ) {
    }
}
