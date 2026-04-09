package com.lexzip.backend.forum;

public record ForumCreateTopicRequest(
        String title,
        String content,
        String category,
        String guestName,
        String guestProfession
) {
}
