package com.shg.service;

import com.shg.model.MonthlyReport;
import com.shg.model.SHGGroup;
import com.shg.model.Transaction;
import com.shg.factory.FinancialRecordFactory;
import com.shg.repository.SHGGroupRepository;
import com.shg.repository.MonthlyReportRepository;
import com.shg.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class MonthlyReportService {

    private final MonthlyReportRepository reportRepository;
    private final TransactionRepository transactionRepository;
    private final SHGGroupRepository shgGroupRepository;
    private final FinancialRecordFactory financialRecordFactory;

    public MonthlyReportService(MonthlyReportRepository reportRepository,
                                TransactionRepository transactionRepository,
                                SHGGroupRepository shgGroupRepository,
                                FinancialRecordFactory financialRecordFactory) {
        this.reportRepository = reportRepository;
        this.transactionRepository = transactionRepository;
        this.shgGroupRepository = shgGroupRepository;
        this.financialRecordFactory = financialRecordFactory;
    }

    public MonthlyReport generateMonthlyReport(Long shgGroupId, Integer month, Integer year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        SHGGroup group = shgGroupRepository.findById(shgGroupId)
                .orElseThrow(() -> new IllegalArgumentException("SHG Group not found"));
        List<Transaction> transactions = transactionRepository.findByShgGroupIdAndTransactionDateBetween(shgGroupId, startDate, endDate);

        double totalSavings = sumByType(transactions, "SAVINGS");
        double totalLoans = sumByType(transactions, "LOAN");
        double totalExpenses = sumByType(transactions, "EXPENSE");
        double totalBalance = totalSavings - totalLoans - totalExpenses;

        MonthlyReport report = reportRepository.findByShgGroupIdAndMonthAndYear(shgGroupId, month, year)
                .orElseGet(() -> financialRecordFactory.createMonthlyReport(
                        month,
                        year,
                        group,
                        totalSavings,
                        totalLoans,
                        totalExpenses,
                        transactions.size()));
        report.setMonth(month);
        report.setYear(year);
        report.setShgGroup(group);
        report.setTotalSavings(totalSavings);
        report.setTotalLoans(totalLoans);
        report.setTotalExpenses(totalExpenses);
        report.setTotalBalance(totalBalance);
        report.setTransactionCount(transactions.size());

        return reportRepository.save(report);
    }

    public List<MonthlyReport> getReportsByShgGroupId(Long shgGroupId) {
        return reportRepository.findByShgGroupId(shgGroupId);
    }

    public Optional<MonthlyReport> getReportByShgGroupAndMonth(Long shgGroupId, Integer month, Integer year) {
        return reportRepository.findByShgGroupIdAndMonthAndYear(shgGroupId, month, year);
    }

    private double sumByType(List<Transaction> transactions, String type) {
        return transactions.stream()
                .filter(transaction -> type.equals(transaction.getType() == null
                        ? ""
                        : transaction.getType().trim().toUpperCase(Locale.ENGLISH)))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
}
