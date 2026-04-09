package com.lexzip.backend.forum;

public record ForumCreateReplyRequest(
        String content,
        String guestName
) {
}
