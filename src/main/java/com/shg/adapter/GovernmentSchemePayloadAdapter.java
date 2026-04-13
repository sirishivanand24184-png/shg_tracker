package com.shg.adapter;

import com.shg.model.GovernmentScheme;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class GovernmentSchemePayloadAdapter implements ApiPayloadAdapter<GovernmentScheme> {

    @Override
    public Map<String, Object> adapt(GovernmentScheme scheme) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id", scheme.getId());
        payload.put("name", scheme.getSchemeName());
        payload.put("type", "Scheme");
        payload.put("eligibility", scheme.getEligibility() == null ? "" : scheme.getEligibility());
        payload.put("benefit", scheme.getDescription() == null ? "" : scheme.getDescription());
        payload.put("description", scheme.getGovernmentBody());
        return payload;
    }
}
