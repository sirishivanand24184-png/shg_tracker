package com.shg.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shg_members")
public class SHGMember {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(unique = true)
    private String email;
    
    @Column(unique = true)
    private String phoneNumber;
    
    @Column(nullable = false)
    private String role;
    
    @Column(nullable = false)
    private Double savingsAmount = 0.0;
    
    @Column(nullable = false)
    private Double loanAmount = 0.0;
    
    @Column(nullable = false)
    private String status = "ACTIVE";
    
    @Column(name = "joined_date", nullable = false)
    private LocalDateTime joinedDate = LocalDateTime.now();
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shg_group_id", nullable = false)
    private SHGGroup shgGroup;

    // Constructors
    public SHGMember() {}

    public SHGMember(String username, String password, String fullName, String role, SHGGroup shgGroup) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.shgGroup = shgGroup;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Double getSavingsAmount() { return savingsAmount; }
    public void setSavingsAmount(Double savingsAmount) { this.savingsAmount = savingsAmount; }

    public Double getLoanAmount() { return loanAmount; }
    public void setLoanAmount(Double loanAmount) { this.loanAmount = loanAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getJoinedDate() { return joinedDate; }
    public void setJoinedDate(LocalDateTime joinedDate) { this.joinedDate = joinedDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public SHGGroup getShgGroup() { return shgGroup; }
    public void setShgGroup(SHGGroup shgGroup) { this.shgGroup = shgGroup; }
}