package com.lexzip.backend.article;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<ArticleResponse> getArticles(@RequestParam(required = false) String category) {
        return articleService.getArticles(category);
    }

    @GetMapping("/categories")
    public List<ArticleCategoryResponse> getCategories() {
        return articleService.getCategories();
    }

    @GetMapping("/{category}/{number}")
    public ArticleResponse getArticle(@PathVariable String category, @PathVariable Integer number) {
        return articleService.getArticle(category, number);
    }
}
