package com.shg.service;

import com.shg.model.GovernmentScheme;
import com.shg.model.InvestmentPlan;
import com.shg.model.Recommendation;
import com.shg.repository.GovernmentSchemeRepository;
import com.shg.repository.InvestmentPlanRepository;
import com.shg.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdvisoryService {

    private final InvestmentPlanRepository investmentPlanRepository;
    private final GovernmentSchemeRepository governmentSchemeRepository;
    private final RecommendationRepository recommendationRepository;

    public AdvisoryService(InvestmentPlanRepository investmentPlanRepository,
                           GovernmentSchemeRepository governmentSchemeRepository,
                           RecommendationRepository recommendationRepository) {
        this.investmentPlanRepository = investmentPlanRepository;
        this.governmentSchemeRepository = governmentSchemeRepository;
        this.recommendationRepository = recommendationRepository;
    }

    public List<InvestmentPlan> getApprovedInvestmentPlans() {
        return investmentPlanRepository.findByStatus("APPROVED")
                .stream()
                .sorted(Comparator.comparing(InvestmentPlan::getExpectedReturn).reversed())
                .collect(Collectors.toList());
    }

    public List<GovernmentScheme> getGovernmentSchemes() {
        return governmentSchemeRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(GovernmentScheme::getSchemeName))
                .collect(Collectors.toList());
    }

    public List<Recommendation> getApprovedRecommendations() {
        return recommendationRepository.findByStatus("APPROVED")
                .stream()
                .sorted(Comparator.comparing(Recommendation::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }
}
