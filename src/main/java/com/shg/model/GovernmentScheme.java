package com.shg.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "government_schemes")
public class GovernmentScheme {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String schemeName;
    
    @Column(length = 500)
    private String description;
    
    @Column(length = 1000)
    private String eligibility;
    
    @Column(nullable = false)
    private Double maxLoanAmount;
    
    @Column(nullable = false)
    private Double interestRate;
    
    @Column(nullable = false)
    private Integer repaymentPeriodMonths;
    
    @Column(nullable = false)
    private String governmentBody;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructors
    public GovernmentScheme() {}

    public GovernmentScheme(String schemeName, Double maxLoanAmount, Double interestRate, Integer repaymentPeriodMonths, String governmentBody) {
        this.schemeName = schemeName;
        this.maxLoanAmount = maxLoanAmount;
        this.interestRate = interestRate;
        this.repaymentPeriodMonths = repaymentPeriodMonths;
        this.governmentBody = governmentBody;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSchemeName() { return schemeName; }
    public void setSchemeName(String schemeName) { this.schemeName = schemeName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEligibility() { return eligibility; }
    public void setEligibility(String eligibility) { this.eligibility = eligibility; }

    public Double getMaxLoanAmount() { return maxLoanAmount; }
    public void setMaxLoanAmount(Double maxLoanAmount) { this.maxLoanAmount = maxLoanAmount; }

    public Double getInterestRate() { return interestRate; }
    public void setInterestRate(Double interestRate) { this.interestRate = interestRate; }

    public Integer getRepaymentPeriodMonths() { return repaymentPeriodMonths; }
    public void setRepaymentPeriodMonths(Integer repaymentPeriodMonths) { this.repaymentPeriodMonths = repaymentPeriodMonths; }

    public String getGovernmentBody() { return governmentBody; }
    public void setGovernmentBody(String governmentBody) { this.governmentBody = governmentBody; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}