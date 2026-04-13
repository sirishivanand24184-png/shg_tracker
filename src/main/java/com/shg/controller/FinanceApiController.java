package com.shg.controller;

import com.shg.facade.FinanceFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FinanceApiController {

    private final FinanceFacade financeFacade;

    public FinanceApiController(FinanceFacade financeFacade) {
        this.financeFacade = financeFacade;
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Map<String, Object>>> getTransactions() {
        return ResponseEntity.ok(financeFacade.getTransactions());
    }

    @PostMapping("/transactions")
    public ResponseEntity<Map<String, Object>> createTransaction(@RequestBody Map<String, String> payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(financeFacade.createTransaction(payload));
    }

    @GetMapping("/reports/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyReports() {
        return ResponseEntity.ok(financeFacade.getMonthlyReports());
    }
}
