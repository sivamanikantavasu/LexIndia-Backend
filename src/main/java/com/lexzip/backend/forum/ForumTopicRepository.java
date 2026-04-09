package com.lexzip.backend.forum;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ForumTopicRepository extends JpaRepository<ForumTopic, UUID> {
    List<ForumTopic> findAllByOrderByCreatedAtDesc();

    List<ForumTopic> findByCategoryOrderByCreatedAtDesc(String category);
}
