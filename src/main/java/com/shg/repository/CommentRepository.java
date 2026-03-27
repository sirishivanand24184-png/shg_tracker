package com.shg.repository;

import com.shg.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByDiscussionId(Long discussionId);
    List<Comment> findByAuthorUsername(String username);
}