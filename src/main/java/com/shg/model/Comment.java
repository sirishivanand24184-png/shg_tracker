package com.shg.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 1000, nullable = false)
    private String content;
    
    @Column(nullable = false)
    private String authorUsername;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id", nullable = false)
    private Discussion discussion;

    // Constructors
    public Comment() {}

    public Comment(String content, String authorUsername, Discussion discussion) {
        this.content = content;
        this.authorUsername = authorUsername;
        this.discussion = discussion;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Discussion getDiscussion() { return discussion; }
    public void setDiscussion(Discussion discussion) { this.discussion = discussion; }
}