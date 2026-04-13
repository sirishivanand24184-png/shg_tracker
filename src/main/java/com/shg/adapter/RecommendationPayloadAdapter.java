package com.shg.adapter;

import com.shg.model.Recommendation;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class RecommendationPayloadAdapter implements ApiPayloadAdapter<Recommendation> {

    @Override
    public Map<String, Object> adapt(Recommendation recommendation) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id", recommendation.getId());
        payload.put("title", recommendation.getTitle());
        payload.put("reason", recommendation.getDescription() == null ? "" : recommendation.getDescription());
        payload.put("priority", recommendation.getPriority());
        return payload;
    }
}
