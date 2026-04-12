package com.shg.service;

import com.shg.model.MonthlyReport;
import com.shg.model.SHGGroup;
import com.shg.model.SHGMember;
import com.shg.model.Transaction;
import com.shg.repository.SHGGroupRepository;
import com.shg.repository.SHGMemberRepository;
import com.shg.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final SHGGroupRepository shgGroupRepository;
    private final SHGMemberRepository shgMemberRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              SHGGroupRepository shgGroupRepository,
                              SHGMemberRepository shgMemberRepository) {
        this.transactionRepository = transactionRepository;
        this.shgGroupRepository = shgGroupRepository;
        this.shgMemberRepository = shgMemberRepository;
    }

    public Transaction createTransaction(Transaction transaction) {
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setType(normalizeType(transaction.getType()));
        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(LocalDateTime.now());
        }

        Transaction saved = transactionRepository.save(transaction);
        updateAggregates(saved);
        return saved;
    }

    private void updateAggregates(Transaction transaction) {
        SHGGroup group = transaction.getShgGroup();
        if (group != null) {
            double currentBalance = group.getTotalBalance() == null ? 0.0 : group.getTotalBalance();
            switch (transaction.getType()) {
                case "SAVINGS":
                    group.setTotalBalance(currentBalance + transaction.getAmount());
                    break;
                case "EXPENSE":
                case "LOAN":
                    group.setTotalBalance(currentBalance - transaction.getAmount());
                    break;
                default:
                    break;
            }
            group.setUpdatedAt(LocalDateTime.now());
            shgGroupRepository.save(group);
        }

        SHGMember member = transaction.getMember();
        if (member != null) {
            if ("SAVINGS".equals(transaction.getType())) {
                member.setSavingsAmount(member.getSavingsAmount() + transaction.getAmount());
            } else if ("LOAN".equals(transaction.getType())) {
                member.setLoanAmount(member.getLoanAmount() + transaction.getAmount());
            }
            member.setUpdatedAt(LocalDateTime.now());
            shgMemberRepository.save(member);
        }
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> getTransactionsByShgGroupId(Long shgGroupId) {
        return transactionRepository.findByShgGroupId(shgGroupId);
    }

    public List<Transaction> getTransactionsByType(String type) {
        return transactionRepository.findAll().stream()
                .filter(transaction -> normalizeType(transaction.getType()).equals(normalizeType(type)))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByTransactionDateBetween(startDate, endDate);
    }

    public List<Transaction> getTransactionsByShgGroupAndDateRange(Long shgGroupId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByShgGroupIdAndTransactionDateBetween(shgGroupId, startDate, endDate);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .sorted(Comparator.comparing(Transaction::getTransactionDate).reversed())
                .collect(Collectors.toList());
    }

    public List<MonthlyReport> generateMonthlySnapshots(Long shgGroupId) {
        List<Transaction> transactions = getTransactionsByShgGroupId(shgGroupId);
        List<YearMonth> months = transactions.stream()
                .map(transaction -> YearMonth.from(transaction.getTransactionDate()))
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        List<MonthlyReport> reports = new ArrayList<>();
        for (YearMonth yearMonth : months) {
            MonthlyReport report = new MonthlyReport();
            report.setMonth(yearMonth.getMonthValue());
            report.setYear(yearMonth.getYear());
            report.setShgGroup(shgGroupRepository.findById(shgGroupId).orElse(null));

            List<Transaction> monthlyTransactions = transactions.stream()
                    .filter(transaction -> YearMonth.from(transaction.getTransactionDate()).equals(yearMonth))
                    .collect(Collectors.toList());

            report.setTotalSavings(sumByType(monthlyTransactions, "SAVINGS"));
            report.setTotalLoans(sumByType(monthlyTransactions, "LOAN"));
            report.setTotalExpenses(sumByType(monthlyTransactions, "EXPENSE"));
            report.setTotalBalance(report.getTotalSavings() - report.getTotalLoans() - report.getTotalExpenses());
            report.setTransactionCount(monthlyTransactions.size());
            reports.add(report);
        }
        return reports;
    }

    private double sumByType(List<Transaction> transactions, String type) {
        return transactions.stream()
                .filter(transaction -> type.equals(normalizeType(transaction.getType())))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    private String normalizeType(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.trim().toUpperCase(Locale.ENGLISH);
    }
}
