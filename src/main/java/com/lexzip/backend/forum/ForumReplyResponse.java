package com.lexzip.backend.forum;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ForumReplyResponse(
        UUID id,
        String content,
        String authorName,
        OffsetDateTime createdAt
) {
    static ForumReplyResponse from(ForumReply reply) {
        return new ForumReplyResponse(
                reply.getId(),
                reply.getContent(),
                reply.getAuthor() != null ? reply.getAuthor().getFullName() : fallback(reply.getGuestName(), "Community Member"),
                reply.getCreatedAt()
        );
    }

    private static String fallback(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
