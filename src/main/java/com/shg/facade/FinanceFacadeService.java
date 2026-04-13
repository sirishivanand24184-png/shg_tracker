package com.shg.facade;

import com.shg.factory.FinancialRecordFactory;
import com.shg.model.MonthlyReport;
import com.shg.model.SHGGroup;
import com.shg.model.SHGMember;
import com.shg.model.Transaction;
import com.shg.repository.SHGGroupRepository;
import com.shg.repository.SHGMemberRepository;
import com.shg.service.MonthlyReportService;
import com.shg.service.TransactionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FinanceFacadeService implements FinanceFacade {

    private final TransactionService transactionService;
    private final MonthlyReportService monthlyReportService;
    private final SHGGroupRepository shgGroupRepository;
    private final SHGMemberRepository shgMemberRepository;
    private final FinancialRecordFactory financialRecordFactory;

    public FinanceFacadeService(TransactionService transactionService,
                                MonthlyReportService monthlyReportService,
                                SHGGroupRepository shgGroupRepository,
                                SHGMemberRepository shgMemberRepository,
                                FinancialRecordFactory financialRecordFactory) {
        this.transactionService = transactionService;
        this.monthlyReportService = monthlyReportService;
        this.shgGroupRepository = shgGroupRepository;
        this.shgMemberRepository = shgMemberRepository;
        this.financialRecordFactory = financialRecordFactory;
    }

    @Override
    public List<Map<String, Object>> getTransactions() {
        return transactionService.getAllTransactions()
                .stream()
                .map(this::toTransactionPayload)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> createTransaction(Map<String, String> payload) {
        SHGGroup group = getPrimaryGroup();
        Optional<SHGMember> member = resolveMember(payload.getOrDefault("member", ""));

        Transaction transaction = financialRecordFactory.createTransaction(
                payload.getOrDefault("type", "Savings"),
                Double.parseDouble(payload.getOrDefault("amount", "0")),
                payload.getOrDefault("description", ""),
                payload.getOrDefault("member", "System"),
                parseDate(payload.get("date")),
                group,
                member.orElse(null));

        Transaction saved = transactionService.createTransaction(transaction);
        return toTransactionPayload(saved);
    }

    @Override
    public Map<String, Object> getMonthlyReports() {
        SHGGroup group = getPrimaryGroup();
        List<Map<String, Object>> months = monthlyReportService.getReportsByShgGroupId(group.getId()).stream()
                .sorted((left, right) -> {
                    int yearCompare = left.getYear().compareTo(right.getYear());
                    return yearCompare != 0 ? yearCompare : left.getMonth().compareTo(right.getMonth());
                })
                .map(this::toReportPayload)
                .collect(Collectors.toList());

        if (months.isEmpty()) {
            months = transactionService.generateMonthlySnapshots(group.getId()).stream()
                    .map(this::toReportPayload)
                    .collect(Collectors.toList());
        }

        return Map.of("months", months);
    }

    private SHGGroup getPrimaryGroup() {
        return shgGroupRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No SHG group available"));
    }

    private Optional<SHGMember> resolveMember(String memberName) {
        return shgMemberRepository.findAll().stream()
                .filter(candidate -> candidate.getFullName().equalsIgnoreCase(memberName))
                .findFirst();
    }

    private Map<String, Object> toTransactionPayload(Transaction transaction) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id", transaction.getId());
        payload.put("type", capitalize(transaction.getType()));
        payload.put("amount", transaction.getAmount());
        payload.put("date", transaction.getTransactionDate().toLocalDate().toString());
        payload.put("description", transaction.getDescription() == null ? "" : transaction.getDescription());
        payload.put("member", transaction.getMember() != null ? transaction.getMember().getFullName() : transaction.getRecordedBy());
        return payload;
    }

    private Map<String, Object> toReportPayload(MonthlyReport report) {
        String label = report.getMonth() == null ? "Unknown" :
                java.time.Month.of(report.getMonth()).getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                        + " " + report.getYear();
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("month", label);
        payload.put("savings", report.getTotalSavings());
        payload.put("loans", report.getTotalLoans());
        payload.put("expenses", report.getTotalExpenses());
        payload.put("balance", report.getTotalBalance());
        payload.put("transactions", report.getTransactionCount());
        return payload;
    }

    private LocalDateTime parseDate(String value) {
        if (value == null || value.isBlank()) {
            return LocalDateTime.now();
        }
        return LocalDate.parse(value).atStartOfDay();
    }

    private String capitalize(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        String lower = value.toLowerCase(Locale.ENGLISH);
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
