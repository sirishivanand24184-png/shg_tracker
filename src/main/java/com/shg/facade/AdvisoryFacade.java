package com.shg.facade;

import java.util.List;
import java.util.Map;

public interface AdvisoryFacade {
    List<Map<String, Object>> getInvestmentPlans();
    List<Map<String, Object>> getGovernmentSchemes();
    List<Map<String, Object>> getRecommendations();
}
