package com.shg.controller;

import com.shg.model.MonthlyReport;
import com.shg.model.SHGGroup;
import com.shg.model.SHGMember;
import com.shg.model.Transaction;
import com.shg.repository.SHGGroupRepository;
import com.shg.repository.SHGMemberRepository;
import com.shg.service.MonthlyReportService;
import com.shg.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FinanceApiController {

    private final TransactionService transactionService;
    private final MonthlyReportService monthlyReportService;
    private final SHGGroupRepository shgGroupRepository;
    private final SHGMemberRepository shgMemberRepository;

    public FinanceApiController(TransactionService transactionService,
                                MonthlyReportService monthlyReportService,
                                SHGGroupRepository shgGroupRepository,
                                SHGMemberRepository shgMemberRepository) {
        this.transactionService = transactionService;
        this.monthlyReportService = monthlyReportService;
        this.shgGroupRepository = shgGroupRepository;
        this.shgMemberRepository = shgMemberRepository;
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Map<String, Object>>> getTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions()
                .stream()
                .sorted((left, right) -> right.getTransactionDate().compareTo(left.getTransactionDate()))
                .map(this::toTransactionPayload)
                .collect(Collectors.toList()));
    }

    @PostMapping("/transactions")
    public ResponseEntity<Map<String, Object>> createTransaction(@RequestBody Map<String, String> payload) {
        SHGGroup group = shgGroupRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No SHG group available"));

        Transaction transaction = new Transaction();
        transaction.setType(payload.getOrDefault("type", "Savings"));
        transaction.setAmount(Double.parseDouble(payload.getOrDefault("amount", "0")));
        transaction.setDescription(payload.getOrDefault("description", ""));
        transaction.setRecordedBy(payload.getOrDefault("member", "System"));
        transaction.setTransactionDate(parseDate(payload.get("date")));
        transaction.setShgGroup(group);

        Optional<SHGMember> member = shgMemberRepository.findAll().stream()
                .filter(candidate -> candidate.getFullName().equalsIgnoreCase(payload.getOrDefault("member", "")))
                .findFirst();
        member.ifPresent(transaction::setMember);

        Transaction saved = transactionService.createTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(toTransactionPayload(saved));
    }

    @GetMapping("/reports/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyReports() {
        SHGGroup group = shgGroupRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No SHG group available"));

        List<Map<String, Object>> months = monthlyReportService.getReportsByShgGroupId(group.getId()).stream()
                .sorted((left, right) -> {
                    int yearCompare = left.getYear().compareTo(right.getYear());
                    return yearCompare != 0 ? yearCompare : left.getMonth().compareTo(right.getMonth());
                })
                .map(this::toReportPayload)
                .collect(Collectors.toList());

        if (months.isEmpty()) {
            List<Map<String, Object>> generated = transactionService.generateMonthlySnapshots(group.getId()).stream()
                    .map(this::toReportPayload)
                    .collect(Collectors.toList());
            months = generated;
        }

        return ResponseEntity.ok(Map.of("months", months));
    }

    private Map<String, Object> toTransactionPayload(Transaction transaction) {
        return Map.of(
                "id", transaction.getId(),
                "type", capitalize(transaction.getType()),
                "amount", transaction.getAmount(),
                "date", transaction.getTransactionDate().toLocalDate().toString(),
                "description", transaction.getDescription() == null ? "" : transaction.getDescription(),
                "member", transaction.getMember() != null ? transaction.getMember().getFullName() : transaction.getRecordedBy());
    }

    private Map<String, Object> toReportPayload(MonthlyReport report) {
        String label = report.getMonth() == null ? "Unknown" :
                java.time.Month.of(report.getMonth()).getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                        + " " + report.getYear();
        return Map.of(
                "month", label,
                "savings", report.getTotalSavings(),
                "loans", report.getTotalLoans(),
                "expenses", report.getTotalExpenses(),
                "balance", report.getTotalBalance());
    }

    private LocalDateTime parseDate(String date) {
        if (date == null || date.isBlank()) {
            return LocalDateTime.now();
        }
        return LocalDate.parse(date).atStartOfDay();
    }

    private String capitalize(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        String lower = value.toLowerCase(Locale.ENGLISH);
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
