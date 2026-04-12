package com.shg.controller;

import com.shg.model.SHGMember;
import com.shg.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminApiController {

    private final AdminService adminService;

    public AdminApiController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/brokers/pending")
    public ResponseEntity<List<Map<String, Object>>> getPendingBrokers() {
        return ResponseEntity.ok(adminService.getPendingBrokers()
                .stream()
                .map(this::toBrokerPayload)
                .collect(Collectors.toList()));
    }

    @PutMapping("/brokers/{id}/{action}")
    public ResponseEntity<Map<String, Object>> verifyBroker(@PathVariable Long id,
                                                            @PathVariable String action) {
        boolean approved = "approve".equalsIgnoreCase(action);
        SHGMember broker = adminService.verifyBroker(id, approved);
        return ResponseEntity.ok(toBrokerPayload(broker));
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(adminService.getStatistics());
    }

    @GetMapping("/settings")
    public ResponseEntity<Map<String, String>> getSettings() {
        return ResponseEntity.ok(adminService.getSettings());
    }

    @PostMapping("/settings")
    public ResponseEntity<Map<String, String>> updateSettings(@RequestBody Map<String, String> payload) {
        return ResponseEntity.ok(adminService.updateSettings(payload));
    }

    private Map<String, Object> toBrokerPayload(SHGMember member) {
        return Map.of(
                "id", member.getId(),
                "name", member.getFullName(),
                "organisation", member.getEmail() == null ? "Independent Broker" : member.getEmail(),
                "licenceNo", member.getUsername().toUpperCase(),
                "email", member.getEmail() == null ? "" : member.getEmail(),
                "phone", member.getPhoneNumber() == null ? "" : member.getPhoneNumber(),
                "appliedAt", member.getCreatedAt().toString(),
                "experience", member.getRole() + " applicant");
    }
}
