package com.shg.factory;

import com.shg.builder.MonthlyReportBuilder;
import com.shg.builder.TransactionBuilder;
import com.shg.model.MonthlyReport;
import com.shg.model.SHGGroup;
import com.shg.model.SHGMember;
import com.shg.model.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DefaultFinancialRecordFactory implements FinancialRecordFactory {

    @Override
    public Transaction createTransaction(String type,
                                         Double amount,
                                         String description,
                                         String recordedBy,
                                         LocalDateTime transactionDate,
                                         SHGGroup shgGroup,
                                         SHGMember member) {
        return new TransactionBuilder()
                .withType(type)
                .withAmount(amount)
                .withDescription(description)
                .withRecordedBy(recordedBy)
                .withTransactionDate(transactionDate)
                .withShgGroup(shgGroup)
                .withMember(member)
                .build();
    }

    @Override
    public MonthlyReport createMonthlyReport(Integer month,
                                             Integer year,
                                             SHGGroup shgGroup,
                                             Double totalSavings,
                                             Double totalLoans,
                                             Double totalExpenses,
                                             Integer transactionCount) {
        double totalBalance = totalSavings - totalLoans - totalExpenses;
        return new MonthlyReportBuilder()
                .withMonth(month)
                .withYear(year)
                .withShgGroup(shgGroup)
                .withTotalSavings(totalSavings)
                .withTotalLoans(totalLoans)
                .withTotalExpenses(totalExpenses)
                .withTotalBalance(totalBalance)
                .withTransactionCount(transactionCount)
                .build();
    }
}
