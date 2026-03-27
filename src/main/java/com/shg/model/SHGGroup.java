package com.shg.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shg_groups")
public class SHGGroup {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private String location;
    
    @Column(nullable = false)
    private Double totalBalance = 0.0;
    
    @Column(nullable = false)
    private Double monthlyContribution = 0.0;
    
    @Column(nullable = false)
    private String status = "ACTIVE";
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @OneToMany(mappedBy = "shgGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SHGMember> members;
    
    @OneToMany(mappedBy = "shgGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;
    
    @OneToMany(mappedBy = "shgGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonthlyReport> monthlyReports;

    // Constructors
    public SHGGroup() {}

    public SHGGroup(String name, String description, String location, Double monthlyContribution) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.monthlyContribution = monthlyContribution;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Double getMonthlyContribution() {
        return monthlyContribution;
    }

    public void setMonthlyContribution(Double monthlyContribution) {
        this.monthlyContribution = monthlyContribution;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<SHGMember> getMembers() {
        return members;
    }

    public void setMembers(List<SHGMember> members) {
        this.members = members;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<MonthlyReport> getMonthlyReports() {
        return monthlyReports;
    }

    public void setMonthlyReports(List<MonthlyReport> monthlyReports) {
        this.monthlyReports = monthlyReports;
    }
}