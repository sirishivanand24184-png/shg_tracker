package com.shg.controller;

import com.shg.model.MonthlyReport;
import com.shg.service.MonthlyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/report-records")
@CrossOrigin(origins = "*")
public class MonthlyReportController {
    
    @Autowired
    private MonthlyReportService reportService;
    
    @GetMapping("/generate/{shgGroupId}/{month}/{year}")
    public ResponseEntity<MonthlyReport> generateReport(
            @PathVariable Long shgGroupId,
            @PathVariable Integer month,
            @PathVariable Integer year) {
        MonthlyReport report = reportService.generateMonthlyReport(shgGroupId, month, year);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }
    
    @GetMapping("/shg-group/{shgGroupId}")
    public ResponseEntity<List<MonthlyReport>> getReportsByShgGroupId(@PathVariable Long shgGroupId) {
        return ResponseEntity.ok(reportService.getReportsByShgGroupId(shgGroupId));
    }
    
    @GetMapping("/shg-group/{shgGroupId}/month/{month}/year/{year}")
    public ResponseEntity<MonthlyReport> getReportByShgGroupAndMonth(
            @PathVariable Long shgGroupId,
            @PathVariable Integer month,
            @PathVariable Integer year) {
        Optional<MonthlyReport> report = reportService.getReportByShgGroupAndMonth(shgGroupId, month, year);
        return report.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
