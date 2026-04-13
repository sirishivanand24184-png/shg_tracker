package com.shg.facade;

import java.util.List;
import java.util.Map;

public interface FinanceFacade {
    List<Map<String, Object>> getTransactions();
    Map<String, Object> createTransaction(Map<String, String> payload);
    Map<String, Object> getMonthlyReports();
}
