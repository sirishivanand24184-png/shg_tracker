package com.shg.facade;

import com.shg.adapter.GovernmentSchemePayloadAdapter;
import com.shg.adapter.InvestmentPlanPayloadAdapter;
import com.shg.adapter.RecommendationPayloadAdapter;
import com.shg.service.AdvisoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdvisoryFacadeService implements AdvisoryFacade {

    private final AdvisoryService advisoryService;
    private final InvestmentPlanPayloadAdapter investmentPlanPayloadAdapter;
    private final GovernmentSchemePayloadAdapter governmentSchemePayloadAdapter;
    private final RecommendationPayloadAdapter recommendationPayloadAdapter;

    public AdvisoryFacadeService(AdvisoryService advisoryService,
                                 InvestmentPlanPayloadAdapter investmentPlanPayloadAdapter,
                                 GovernmentSchemePayloadAdapter governmentSchemePayloadAdapter,
                                 RecommendationPayloadAdapter recommendationPayloadAdapter) {
        this.advisoryService = advisoryService;
        this.investmentPlanPayloadAdapter = investmentPlanPayloadAdapter;
        this.governmentSchemePayloadAdapter = governmentSchemePayloadAdapter;
        this.recommendationPayloadAdapter = recommendationPayloadAdapter;
    }

    @Override
    public List<Map<String, Object>> getInvestmentPlans() {
        return advisoryService.getApprovedInvestmentPlans().stream()
                .map(investmentPlanPayloadAdapter::adapt)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getGovernmentSchemes() {
        return advisoryService.getGovernmentSchemes().stream()
                .map(governmentSchemePayloadAdapter::adapt)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getRecommendations() {
        return advisoryService.getApprovedRecommendations().stream()
                .map(recommendationPayloadAdapter::adapt)
                .collect(Collectors.toList());
    }
}
