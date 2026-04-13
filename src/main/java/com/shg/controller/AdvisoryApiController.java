package com.shg.controller;

import com.shg.facade.AdvisoryFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/advisory")
@CrossOrigin(origins = "*")
public class AdvisoryApiController {

    private final AdvisoryFacade advisoryFacade;

    public AdvisoryApiController(AdvisoryFacade advisoryFacade) {
        this.advisoryFacade = advisoryFacade;
    }

    @GetMapping("/investment-plans")
    public ResponseEntity<List<Map<String, Object>>> getInvestmentPlans() {
        return ResponseEntity.ok(advisoryFacade.getInvestmentPlans());
    }

    @GetMapping("/govt-schemes")
    public ResponseEntity<List<Map<String, Object>>> getSchemes() {
        return ResponseEntity.ok(advisoryFacade.getGovernmentSchemes());
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<Map<String, Object>>> getRecommendations() {
        return ResponseEntity.ok(advisoryFacade.getRecommendations());
    }
}
