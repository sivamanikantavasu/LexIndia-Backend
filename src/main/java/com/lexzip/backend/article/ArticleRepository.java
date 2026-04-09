package com.lexzip.backend.article;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
    List<Article> findAllByOrderByCategoryAscNumberAsc();

    List<Article> findByCategoryOrderByNumberAsc(String category);

    Optional<Article> findByCategoryAndNumber(String category, Integer number);
}
