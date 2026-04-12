package com.shg.service;

import com.shg.model.InvestmentPlan;
import com.shg.model.SHGGroup;
import com.shg.model.SHGMember;
import com.shg.model.Transaction;
import com.shg.repository.InvestmentPlanRepository;
import com.shg.repository.SHGGroupRepository;
import com.shg.repository.SHGMemberRepository;
import com.shg.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final SHGMemberRepository memberRepository;
    private final SHGGroupRepository groupRepository;
    private final TransactionRepository transactionRepository;
    private final InvestmentPlanRepository investmentPlanRepository;
    private final AppSettingsService appSettingsService;

    public AdminService(SHGMemberRepository memberRepository,
                        SHGGroupRepository groupRepository,
                        TransactionRepository transactionRepository,
                        InvestmentPlanRepository investmentPlanRepository,
                        AppSettingsService appSettingsService) {
        this.memberRepository = memberRepository;
        this.groupRepository = groupRepository;
        this.transactionRepository = transactionRepository;
        this.investmentPlanRepository = investmentPlanRepository;
        this.appSettingsService = appSettingsService;
    }

    public List<SHGMember> getPendingBrokers() {
        return memberRepository.findByRole("Broker")
                .stream()
                .filter(member -> "PENDING".equalsIgnoreCase(member.getStatus()))
                .collect(Collectors.toList());
    }

    public SHGMember verifyBroker(Long id, boolean approved) {
        SHGMember broker = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Broker not found: " + id));
        broker.setStatus(approved ? "APPROVED" : "REJECTED");
        broker.setUpdatedAt(LocalDateTime.now());
        return memberRepository.save(broker);
    }

    public Map<String, Object> getStatistics() {
        List<SHGMember> members = memberRepository.findAll();
        List<SHGGroup> groups = groupRepository.findAll();
        List<Transaction> transactions = transactionRepository.findAll();
        long approvedBrokers = members.stream()
                .filter(member -> "Broker".equalsIgnoreCase(member.getRole()))
                .filter(member -> "APPROVED".equalsIgnoreCase(member.getStatus()))
                .count();

        List<Map<String, Object>> monthlyGrowth = new ArrayList<>();
        YearMonth start = YearMonth.now().minusMonths(5);
        for (int i = 0; i < 6; i++) {
            YearMonth month = start.plusMonths(i);
            monthlyGrowth.add(Map.<String, Object>of(
                    "month", month.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                    "newMembers", members.stream()
                            .filter(member -> YearMonth.from(member.getCreatedAt()).equals(month))
                            .count()));
        }

        return Map.of(
                "totalUsers", members.size(),
                "totalGroups", groups.size(),
                "totalTx", transactions.size(),
                "totalBrokers", approvedBrokers,
                "monthlyGrowth", monthlyGrowth);
    }

    public Map<String, String> getSettings() {
        return appSettingsService.getAll();
    }

    public Map<String, String> updateSettings(Map<String, String> updates) {
        return appSettingsService.update(updates);
    }

    public List<InvestmentPlan> getPendingPlans() {
        return investmentPlanRepository.findByStatus("PENDING");
    }
}
