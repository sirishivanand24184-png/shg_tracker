package com.shg.repository;

import com.shg.model.MonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyReportRepository extends JpaRepository<MonthlyReport, Long> {
    List<MonthlyReport> findByShgGroupId(Long shgGroupId);
    Optional<MonthlyReport> findByShgGroupIdAndMonthAndYear(Long shgGroupId, Integer month, Integer year);
}