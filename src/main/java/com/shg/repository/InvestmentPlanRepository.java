package com.shg.repository;

import com.shg.model.InvestmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentPlanRepository extends JpaRepository<InvestmentPlan, Long> {
    List<InvestmentPlan> findByStatus(String status);
    List<InvestmentPlan> findByRiskLevel(String riskLevel);
    List<InvestmentPlan> findByBrokerName(String brokerName);
}