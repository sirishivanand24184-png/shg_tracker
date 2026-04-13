package com.shg.factory;

import com.shg.model.MonthlyReport;
import com.shg.model.SHGGroup;
import com.shg.model.SHGMember;
import com.shg.model.Transaction;

import java.time.LocalDateTime;

public interface FinancialRecordFactory {

    Transaction createTransaction(String type,
                                  Double amount,
                                  String description,
                                  String recordedBy,
                                  LocalDateTime transactionDate,
                                  SHGGroup shgGroup,
                                  SHGMember member);

    MonthlyReport createMonthlyReport(Integer month,
                                      Integer year,
                                      SHGGroup shgGroup,
                                      Double totalSavings,
                                      Double totalLoans,
                                      Double totalExpenses,
                                      Integer transactionCount);
}
