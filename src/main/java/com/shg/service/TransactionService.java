package com.shg.service;

import com.shg.model.MonthlyReport;
import com.shg.model.SHGGroup;
import com.shg.model.SHGMember;
import com.shg.model.Transaction;
import com.shg.factory.FinancialRecordFactory;
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
    private final FinancialRecordFactory financialRecordFactory;

    public TransactionService(TransactionRepository transactionRepository,
                              SHGGroupRepository shgGroupRepository,
                              SHGMemberRepository shgMemberRepository,
                              FinancialRecordFactory financialRecordFactory) {
        this.transactionRepository = transactionRepository;
        this.shgGroupRepository = shgGroupRepository;
        this.shgMemberRepository = shgMemberRepository;
        this.financialRecordFactory = financialRecordFactory;
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
            List<Transaction> monthlyTransactions = transactions.stream()
                    .filter(transaction -> YearMonth.from(transaction.getTransactionDate()).equals(yearMonth))
                    .collect(Collectors.toList());

            MonthlyReport report = financialRecordFactory.createMonthlyReport(
                    yearMonth.getMonthValue(),
                    yearMonth.getYear(),
                    shgGroupRepository.findById(shgGroupId).orElse(null),
                    sumByType(monthlyTransactions, "SAVINGS"),
                    sumByType(monthlyTransactions, "LOAN"),
                    sumByType(monthlyTransactions, "EXPENSE"),
                    monthlyTransactions.size());
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
