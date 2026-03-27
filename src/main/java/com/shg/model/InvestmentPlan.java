package com.shg.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "investment_plans")
public class InvestmentPlan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String planName;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private Double minimumAmount;
    
    @Column(nullable = false)
    private Double expectedReturn;
    
    @Column(nullable = false)
    private String riskLevel;
    
    @Column(nullable = false)
    private Integer durationMonths;
    
    @Column(nullable = false)
    private String brokerName;
    
    @Column(nullable = false)
    private String status = "PENDING";
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Constructors
    public InvestmentPlan() {}

    public InvestmentPlan(String planName, Double minimumAmount, Double expectedReturn, String riskLevel, Integer durationMonths, String brokerName) {
        this.planName = planName;
        this.minimumAmount = minimumAmount;
        this.expectedReturn = expectedReturn;
        this.riskLevel = riskLevel;
        this.durationMonths = durationMonths;
        this.brokerName = brokerName;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getMinimumAmount() { return minimumAmount; }
    public void setMinimumAmount(Double minimumAmount) { this.minimumAmount = minimumAmount; }

    public Double getExpectedReturn() { return expectedReturn; }
    public void setExpectedReturn(Double expectedReturn) { this.expectedReturn = expectedReturn; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public Integer getDurationMonths() { return durationMonths; }
    public void setDurationMonths(Integer durationMonths) { this.durationMonths = durationMonths; }

    public String getBrokerName() { return brokerName; }
    public void setBrokerName(String brokerName) { this.brokerName = brokerName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}