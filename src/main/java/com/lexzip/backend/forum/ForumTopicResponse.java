package com.lexzip.backend.forum;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ForumTopicResponse(
        UUID id,
        String title,
        String content,
        String category,
        String authorName,
        String authorProfession,
        OffsetDateTime createdAt,
        int repliesCount,
        List<ForumReplyResponse> replies
) {
    static ForumTopicResponse from(ForumTopic topic, List<ForumReply> replies) {
        return new ForumTopicResponse(
                topic.getId(),
                topic.getTitle(),
                topic.getContent(),
                topic.getCategory(),
                topic.getAuthor() != null ? topic.getAuthor().getFullName() : fallback(topic.getGuestName(), "Community Member"),
                topic.getAuthor() != null ? topic.getAuthor().getRole().getValue() : fallback(topic.getGuestProfession(), "Citizen"),
                topic.getCreatedAt(),
                replies.size(),
                replies.stream().map(ForumReplyResponse::from).toList()
        );
    }

    private static String fallback(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
