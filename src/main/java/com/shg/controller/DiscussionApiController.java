package com.shg.controller;

import com.shg.model.Comment;
import com.shg.model.Discussion;
import com.shg.service.DiscussionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/discussions")
@CrossOrigin(origins = "*")
public class DiscussionApiController {

    private final DiscussionService discussionService;

    public DiscussionApiController(DiscussionService discussionService) {
        this.discussionService = discussionService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getDiscussions() {
        return ResponseEntity.ok(discussionService.getAllDiscussions()
                .stream()
                .map(this::toDiscussionSummary)
                .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createDiscussion(@RequestBody Map<String, String> payload) {
        Discussion discussion = discussionService.createDiscussion(
                payload.getOrDefault("title", "Untitled Discussion"),
                payload.getOrDefault("content", ""),
                payload.getOrDefault("author", "Console User"),
                payload.getOrDefault("category", "General"));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDiscussionDetail(discussion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDiscussion(@PathVariable Long id) {
        return ResponseEntity.ok(toDiscussionDetail(discussionService.getDiscussion(id)));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Map<String, Object>>> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(discussionService.getComments(id)
                .stream()
                .map(this::toCommentPayload)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Map<String, Object>> addComment(@PathVariable Long id,
                                                          @RequestBody Map<String, String> payload) {
        Comment comment = discussionService.addComment(
                id,
                payload.getOrDefault("author", "Console User"),
                payload.getOrDefault("content", ""));
        return ResponseEntity.status(HttpStatus.CREATED).body(toCommentPayload(comment));
    }

    private Map<String, Object> toDiscussionSummary(Discussion discussion) {
        return Map.of(
                "id", discussion.getId(),
                "title", discussion.getTitle(),
                "author", discussion.getCreatedByUsername(),
                "category", discussion.getTopic(),
                "preview", discussion.getContent() == null ? "" : discussion.getContent(),
                "commentCount", discussionService.getComments(discussion.getId()).size(),
                "createdAt", discussion.getCreatedAt().toString());
    }

    private Map<String, Object> toDiscussionDetail(Discussion discussion) {
        return Map.of(
                "id", discussion.getId(),
                "title", discussion.getTitle(),
                "author", discussion.getCreatedByUsername(),
                "category", discussion.getTopic(),
                "content", discussion.getContent() == null ? "" : discussion.getContent(),
                "createdAt", discussion.getCreatedAt().toString());
    }

    private Map<String, Object> toCommentPayload(Comment comment) {
        return Map.of(
                "id", comment.getId(),
                "author", comment.getAuthorUsername(),
                "content", comment.getContent(),
                "createdAt", comment.getCreatedAt().toString());
    }
}
