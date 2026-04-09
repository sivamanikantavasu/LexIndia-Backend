package com.lexzip.backend.forum;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ForumReplyRepository extends JpaRepository<ForumReply, UUID> {
    List<ForumReply> findByTopicIdOrderByCreatedAtAsc(UUID topicId);
}
