package com.shg.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendations")
public class Recommendation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private String recommendationType;
    
    @Column(nullable = false)
    private String priority;
    
    @Column(nullable = false)
    private Double expectedBenefit;
    
    @Column(nullable = false)
    private String generatedBy;
    
    @Column(nullable = false)
    private String status = "PENDING";
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructors
    public Recommendation() {}

    public Recommendation(String title, String recommendationType, String priority, Double expectedBenefit, String generatedBy) {
        this.title = title;
        this.recommendationType = recommendationType;
        this.priority = priority;
        this.expectedBenefit = expectedBenefit;
        this.generatedBy = generatedBy;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRecommendationType() { return recommendationType; }
    public void setRecommendationType(String recommendationType) { this.recommendationType = recommendationType; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public Double getExpectedBenefit() { return expectedBenefit; }
    public void setExpectedBenefit(Double expectedBenefit) { this.expectedBenefit = expectedBenefit; }

    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}