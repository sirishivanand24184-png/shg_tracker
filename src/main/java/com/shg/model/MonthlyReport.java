package com.shg.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "monthly_reports")
public class MonthlyReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "report_month", nullable = false)
    private Integer month;
    
    @Column(name = "report_year", nullable = false)
    private Integer year;
    
    @Column(nullable = false)
    private Double totalSavings = 0.0;
    
    @Column(nullable = false)
    private Double totalLoans = 0.0;
    
    @Column(nullable = false)
    private Double totalExpenses = 0.0;
    
    @Column(nullable = false)
    private Double totalBalance = 0.0;
    
    @Column(nullable = false)
    private Integer transactionCount = 0;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shg_group_id", nullable = false)
    private SHGGroup shgGroup;

    // Constructors
    public MonthlyReport() {}

    public MonthlyReport(Integer month, Integer year, SHGGroup shgGroup) {
        this.month = month;
        this.year = year;
        this.shgGroup = shgGroup;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Double getTotalSavings() { return totalSavings; }
    public void setTotalSavings(Double totalSavings) { this.totalSavings = totalSavings; }

    public Double getTotalLoans() { return totalLoans; }
    public void setTotalLoans(Double totalLoans) { this.totalLoans = totalLoans; }

    public Double getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(Double totalExpenses) { this.totalExpenses = totalExpenses; }

    public Double getTotalBalance() { return totalBalance; }
    public void setTotalBalance(Double totalBalance) { this.totalBalance = totalBalance; }

    public Integer getTransactionCount() { return transactionCount; }
    public void setTransactionCount(Integer transactionCount) { this.transactionCount = transactionCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public SHGGroup getShgGroup() { return shgGroup; }
    public void setShgGroup(SHGGroup shgGroup) { this.shgGroup = shgGroup; }
}
