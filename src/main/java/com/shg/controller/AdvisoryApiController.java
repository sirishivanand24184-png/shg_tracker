package com.shg.controller;

import com.shg.model.GovernmentScheme;
import com.shg.model.InvestmentPlan;
import com.shg.model.Recommendation;
import com.shg.service.AdvisoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/advisory")
@CrossOrigin(origins = "*")
public class AdvisoryApiController {

    private final AdvisoryService advisoryService;

    public AdvisoryApiController(AdvisoryService advisoryService) {
        this.advisoryService = advisoryService;
    }

    @GetMapping("/investment-plans")
    public ResponseEntity<List<Map<String, Object>>> getInvestmentPlans() {
        return ResponseEntity.ok(advisoryService.getApprovedInvestmentPlans()
                .stream()
                .map(this::toPlanPayload)
                .collect(Collectors.toList()));
    }

    @GetMapping("/govt-schemes")
    public ResponseEntity<List<Map<String, Object>>> getSchemes() {
        return ResponseEntity.ok(advisoryService.getGovernmentSchemes()
                .stream()
                .map(this::toSchemePayload)
                .collect(Collectors.toList()));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<Map<String, Object>>> getRecommendations() {
        return ResponseEntity.ok(advisoryService.getApprovedRecommendations()
                .stream()
                .map(this::toRecommendationPayload)
                .collect(Collectors.toList()));
    }

    private Map<String, Object> toPlanPayload(InvestmentPlan plan) {
        return Map.of(
                "id", plan.getId(),
                "name", plan.getPlanName(),
                "provider", plan.getBrokerName(),
                "returnRate", plan.getExpectedReturn(),
                "risk", plan.getRiskLevel(),
                "tenure", plan.getDurationMonths() + " months",
                "minAmount", plan.getMinimumAmount(),
                "type", plan.getDurationMonths() >= 12 ? "FD" : "SIP",
                "description", plan.getDescription() == null ? "" : plan.getDescription());
    }

    private Map<String, Object> toSchemePayload(GovernmentScheme scheme) {
        return Map.of(
                "id", scheme.getId(),
                "name", scheme.getSchemeName(),
                "type", "Scheme",
                "eligibility", scheme.getEligibility() == null ? "" : scheme.getEligibility(),
                "benefit", scheme.getDescription() == null ? "" : scheme.getDescription(),
                "description", scheme.getGovernmentBody());
    }

    private Map<String, Object> toRecommendationPayload(Recommendation recommendation) {
        return Map.of(
                "id", recommendation.getId(),
                "title", recommendation.getTitle(),
                "reason", recommendation.getDescription() == null ? "" : recommendation.getDescription(),
                "priority", recommendation.getPriority());
    }
}
