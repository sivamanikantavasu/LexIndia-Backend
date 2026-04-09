package com.lexzip.backend.article;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory, String> {
    List<ArticleCategory> findAllByOrderByTitleAsc();
}
