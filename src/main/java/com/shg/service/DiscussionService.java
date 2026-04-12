package com.shg.service;

import com.shg.model.Comment;
import com.shg.model.Discussion;
import com.shg.repository.CommentRepository;
import com.shg.repository.DiscussionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscussionService {

    private final DiscussionRepository discussionRepository;
    private final CommentRepository commentRepository;

    public DiscussionService(DiscussionRepository discussionRepository,
                             CommentRepository commentRepository) {
        this.discussionRepository = discussionRepository;
        this.commentRepository = commentRepository;
    }

    public List<Discussion> getAllDiscussions() {
        return discussionRepository.findAll()
                .stream()
                .sorted((left, right) -> right.getCreatedAt().compareTo(left.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public Discussion getDiscussion(Long id) {
        return discussionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discussion not found: " + id));
    }

    public Discussion createDiscussion(String title, String content, String author, String category) {
        Discussion discussion = new Discussion();
        discussion.setTitle(title);
        discussion.setContent(content);
        discussion.setCreatedByUsername(author);
        discussion.setTopic(category);
        discussion.setCreatedAt(LocalDateTime.now());
        discussion.setUpdatedAt(LocalDateTime.now());
        return discussionRepository.save(discussion);
    }

    public List<Comment> getComments(Long discussionId) {
        return commentRepository.findByDiscussionId(discussionId)
                .stream()
                .sorted((left, right) -> left.getCreatedAt().compareTo(right.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public Comment addComment(Long discussionId, String author, String content) {
        Discussion discussion = getDiscussion(discussionId);
        Comment comment = new Comment();
        comment.setDiscussion(discussion);
        comment.setAuthorUsername(author);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        discussion.setUpdatedAt(LocalDateTime.now());
        discussionRepository.save(discussion);
        return commentRepository.save(comment);
    }
}
