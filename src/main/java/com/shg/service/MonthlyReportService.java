package com.shg.service;

import com.shg.model.MonthlyReport;
import com.shg.model.Transaction;
import com.shg.repository.MonthlyReportRepository;
import com.shg.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class MonthlyReportService {
    
    @Autowired
    private MonthlyReportRepository reportRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    public MonthlyReport generateMonthlyReport(Long shgGroupId, Integer month, Integer year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        
        List<Transaction> transactions = transactionRepository.findByShgGroupIdAndTransactionDateBetween(shgGroupId, startDate, endDate);
        
        Double totalSavings = transactions.stream()
            .filter(t -> "SAVINGS".equals(t.getType()))
            .mapToDouble(Transaction::getAmount)
            .sum();
        
        Double totalLoans = transactions.stream()
            .filter(t -> "LOAN".equals(t.getType()))
            .mapToDouble(Transaction::getAmount)
            .sum();
        
        Double totalExpenses = transactions.stream()
            .filter(t -> "EXPENSE".equals(t.getType()))
            .mapToDouble(Transaction::getAmount)
            .sum();
        
        Double totalBalance = totalSavings - totalExpenses;
        
        MonthlyReport report = new MonthlyReport();
        report.setMonth(month);
        report.setYear(year);
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
}