package com.lexzip.backend.forum;

import com.lexzip.backend.auth.AuthService;
import com.lexzip.backend.auth.Profile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    private final ForumTopicRepository forumTopicRepository;
    private final ForumReplyRepository forumReplyRepository;
    private final AuthService authService;

    public ForumController(
            ForumTopicRepository forumTopicRepository,
            ForumReplyRepository forumReplyRepository,
            AuthService authService
    ) {
        this.forumTopicRepository = forumTopicRepository;
        this.forumReplyRepository = forumReplyRepository;
        this.authService = authService;
    }

    @GetMapping("/topics")
    @Transactional(readOnly = true)
    public List<ForumTopicResponse> getTopics(@RequestParam(required = false) String category) {
        List<ForumTopic> topics = category == null || category.isBlank() || "all".equalsIgnoreCase(category)
                ? forumTopicRepository.findAllByOrderByCreatedAtDesc()
                : forumTopicRepository.findByCategoryOrderByCreatedAtDesc(category);

        return topics.stream()
                .map(topic -> ForumTopicResponse.from(topic, forumReplyRepository.findByTopicIdOrderByCreatedAtAsc(topic.getId())))
                .toList();
    }

    @PostMapping("/topics")
    @Transactional
    public ForumTopicResponse createTopic(
            @RequestHeader(name = "X-Session-Token", required = false) String token,
            @RequestBody ForumCreateTopicRequest request
    ) {
        ForumTopic topic = new ForumTopic();
        topic.setTitle(request.title().trim());
        topic.setContent(request.content().trim());
        topic.setCategory(request.category());

        Profile profile = resolveProfile(token);
        if (profile != null) {
            topic.setAuthor(profile);
        } else {
            topic.setGuestName(request.guestName());
            topic.setGuestProfession(request.guestProfession());
        }

        ForumTopic saved = forumTopicRepository.save(topic);
        return ForumTopicResponse.from(saved, List.of());
    }

    @PostMapping("/topics/{topicId}/replies")
    @Transactional
    public ForumReplyResponse createReply(
            @PathVariable UUID topicId,
            @RequestHeader(name = "X-Session-Token", required = false) String token,
            @RequestBody ForumCreateReplyRequest request
    ) {
        ForumTopic topic = forumTopicRepository.findById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("Forum topic not found"));

        ForumReply reply = new ForumReply();
        reply.setTopic(topic);
        reply.setContent(request.content().trim());

        Profile profile = resolveProfile(token);
        if (profile != null) {
            reply.setAuthor(profile);
        } else {
            reply.setGuestName(request.guestName());
        }

        return ForumReplyResponse.from(forumReplyRepository.save(reply));
    }

    private Profile resolveProfile(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        try {
            return authService.requireAuthenticatedProfile(token);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }
}
