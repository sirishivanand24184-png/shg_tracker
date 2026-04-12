package com.shg.service;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AppSettingsService {

    private final Map<String, String> settings = new ConcurrentHashMap<>();

    public AppSettingsService() {
        settings.put("maxLoan", "50000");
        settings.put("minSavings", "500");
        settings.put("interestRate", "12");
    }

    public Map<String, String> getAll() {
        return new LinkedHashMap<>(settings);
    }

    public Map<String, String> update(Map<String, String> updates) {
        updates.forEach((key, value) -> {
            if (value != null) {
                settings.put(key, value);
            }
        });
        return getAll();
    }
}
