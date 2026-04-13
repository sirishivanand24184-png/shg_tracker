package com.shg.builder;

import com.shg.model.MonthlyReport;
import com.shg.model.SHGGroup;

public class MonthlyReportBuilder {

    private final MonthlyReport report;

    public MonthlyReportBuilder() {
        this.report = new MonthlyReport();
    }

    public MonthlyReportBuilder withMonth(Integer month) {
        report.setMonth(month);
        return this;
    }

    public MonthlyReportBuilder withYear(Integer year) {
        report.setYear(year);
        return this;
    }

    public MonthlyReportBuilder withShgGroup(SHGGroup shgGroup) {
        report.setShgGroup(shgGroup);
        return this;
    }

    public MonthlyReportBuilder withTotalSavings(Double totalSavings) {
        report.setTotalSavings(totalSavings);
        return this;
    }

    public MonthlyReportBuilder withTotalLoans(Double totalLoans) {
        report.setTotalLoans(totalLoans);
        return this;
    }

    public MonthlyReportBuilder withTotalExpenses(Double totalExpenses) {
        report.setTotalExpenses(totalExpenses);
        return this;
    }

    public MonthlyReportBuilder withTotalBalance(Double totalBalance) {
        report.setTotalBalance(totalBalance);
        return this;
    }

    public MonthlyReportBuilder withTransactionCount(Integer transactionCount) {
        report.setTransactionCount(transactionCount);
        return this;
    }

    public MonthlyReport build() {
        return report;
    }
}
