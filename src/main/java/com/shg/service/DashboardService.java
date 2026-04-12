package com.shg.service;

import com.shg.model.SHGGroup;
import com.shg.model.SHGMember;
import com.shg.model.Transaction;
import com.shg.repository.SHGGroupRepository;
import com.shg.repository.SHGMemberRepository;
import com.shg.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final SHGGroupRepository groupRepository;
    private final SHGMemberRepository memberRepository;
    private final TransactionRepository transactionRepository;

    public DashboardService(SHGGroupRepository groupRepository,
                            SHGMemberRepository memberRepository,
                            TransactionRepository transactionRepository) {
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
        this.transactionRepository = transactionRepository;
    }

    public Map<String, Object> getDashboardStats() {
        SHGGroup group = groupRepository.findAll()
                .stream()
                .min(Comparator.comparing(SHGGroup::getId))
                .orElse(null);

        List<Transaction> transactions = transactionRepository.findAll();
        double totalSavings = transactions.stream()
                .filter(transaction -> "SAVINGS".equalsIgnoreCase(transaction.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();
        double totalLoans = transactions.stream()
                .filter(transaction -> "LOAN".equalsIgnoreCase(transaction.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        return Map.of(
                "groupName", group != null ? group.getName() : "My SHG Group",
                "groupSince", group != null ? group.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM yyyy")) : "-",
                "memberCount", memberRepository.count(),
                "totalBalance", group != null ? group.getTotalBalance() : 0.0,
                "totalSavings", totalSavings,
                "totalLoans", totalLoans);
    }

    public List<Map<String, Object>> getDashboardMembers() {
        List<SHGMember> members = memberRepository.findAll();
        return members.stream()
                .sorted(Comparator.comparing(SHGMember::getFullName))
                .map(member -> Map.<String, Object>of(
                        "id", member.getId(),
                        "name", member.getFullName(),
                        "role", member.getRole(),
                        "savings", member.getSavingsAmount(),
                        "loans", member.getLoanAmount(),
                        "active", "ACTIVE".equalsIgnoreCase(member.getStatus()) || "APPROVED".equalsIgnoreCase(member.getStatus())))
                .collect(Collectors.toList());
    }
}
