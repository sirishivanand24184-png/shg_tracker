package com.shg.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Lightweight in-memory application core for the console flow.
 */
public class ConsolePlatformService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final List<ConsoleUser> users = new ArrayList<>();
    private final List<ConsoleTransaction> transactions = new ArrayList<>();
    private final List<ConsoleDiscussion> discussions = new ArrayList<>();
    private final List<ConsoleInvestmentPlan> investmentPlans = new ArrayList<>();
    private final List<ConsoleGovernmentScheme> governmentSchemes = new ArrayList<>();
    private final List<ConsoleRecommendation> recommendations = new ArrayList<>();
    private final List<ConsoleBrokerApplication> brokerApplications = new ArrayList<>();
    private final Map<String, String> settings = new LinkedHashMap<>();

    private final String groupName = "Shakti Mahila Sangha";
    private final String groupLocation = "Mysuru";

    public ConsolePlatformService() {
        seedData();
    }

    public Optional<ConsoleUser> authenticate(String username, String password) {
        return users.stream()
                .filter(user -> user.username.equalsIgnoreCase(username) && user.password.equals(password))
                .findFirst();
    }

    public boolean register(String fullName, String username, String password, String role) {
        boolean exists = users.stream().anyMatch(user -> user.username.equalsIgnoreCase(username));
        if (exists) {
            return false;
        }
        users.add(new ConsoleUser(fullName, username, password, role, "Admin".equalsIgnoreCase(role) ? "APPROVED" : "ACTIVE"));
        return true;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getMemberCount() {
        return (int) users.stream().filter(user -> !"Broker".equalsIgnoreCase(user.role)).count();
    }

    public double getGroupBalance() {
        double balance = 0.0;
        for (ConsoleTransaction transaction : transactions) {
            switch (transaction.type) {
                case "Savings":
                    balance += transaction.amount;
                    break;
                case "Loan":
                case "Expense":
                    balance -= transaction.amount;
                    break;
                default:
                    break;
            }
        }
        return balance;
    }

    public List<String> getMemberLabels() {
        return users.stream()
                .filter(user -> !"Broker".equalsIgnoreCase(user.role))
                .sorted(Comparator.comparing(user -> user.fullName))
                .map(user -> String.format("%s (%s)", user.fullName, user.role))
                .collect(Collectors.toList());
    }

    public void recordTransaction(String type, double amount, String description, String dateText, String actor) {
        ConsoleUser member = users.stream()
                .filter(user -> user.fullName.equalsIgnoreCase(actor) || user.username.equalsIgnoreCase(actor))
                .findFirst()
                .orElse(null);
        LocalDate date = LocalDate.parse(dateText, DATE_FORMATTER);
        transactions.add(new ConsoleTransaction(type, amount, description, date, actor, member == null ? "" : member.fullName));
        if (member != null) {
            if ("Savings".equalsIgnoreCase(type)) {
                member.savings += amount;
            } else if ("Loan".equalsIgnoreCase(type)) {
                member.loans += amount;
            }
        }
    }

    public List<String[]> getTransactions(Integer filterChoice, String start, String end) {
        return transactions.stream()
                .filter(transaction -> matchesFilter(transaction, filterChoice, start, end))
                .sorted(Comparator.comparing((ConsoleTransaction tx) -> tx.date).reversed())
                .map(transaction -> new String[]{
                        transaction.date.format(DATE_FORMATTER),
                        transaction.type,
                        String.format(Locale.ENGLISH, "%.2f", transaction.amount),
                        transaction.description
                })
                .collect(Collectors.toList());
    }

    public double[] getBalanceSummary() {
        double savings = transactions.stream().filter(tx -> "Savings".equalsIgnoreCase(tx.type)).mapToDouble(tx -> tx.amount).sum();
        double loans = transactions.stream().filter(tx -> "Loan".equalsIgnoreCase(tx.type)).mapToDouble(tx -> tx.amount).sum();
        double expenses = transactions.stream().filter(tx -> "Expense".equalsIgnoreCase(tx.type)).mapToDouble(tx -> tx.amount).sum();
        double netBalance = savings - loans - expenses;
        return new double[]{savings, loans, expenses, netBalance};
    }

    public List<String[]> getComparativeData() {
        return transactions.stream()
                .collect(Collectors.groupingBy(tx -> YearMonth.from(tx.date), LinkedHashMap::new, Collectors.toList()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    double savings = entry.getValue().stream().filter(tx -> "Savings".equalsIgnoreCase(tx.type)).mapToDouble(tx -> tx.amount).sum();
                    double loans = entry.getValue().stream().filter(tx -> "Loan".equalsIgnoreCase(tx.type)).mapToDouble(tx -> tx.amount).sum();
                    double expenses = entry.getValue().stream().filter(tx -> "Expense".equalsIgnoreCase(tx.type)).mapToDouble(tx -> tx.amount).sum();
                    return new String[]{
                            entry.getKey().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + entry.getKey().getYear(),
                            String.format(Locale.ENGLISH, "%.0f", savings),
                            String.format(Locale.ENGLISH, "%.0f", loans),
                            String.format(Locale.ENGLISH, "%.0f", expenses)
                    };
                })
                .collect(Collectors.toList());
    }

    public List<String[]> getInvestmentComparison() {
        return investmentPlans.stream()
                .sorted(Comparator.comparing(plan -> plan.planName))
                .map(plan -> new String[]{
                        plan.planName,
                        String.format(Locale.ENGLISH, "%.1f", plan.expectedReturn),
                        plan.riskLevel,
                        plan.tenure
                })
                .collect(Collectors.toList());
    }

    public List<String[]> getBrokerPlans() {
        return investmentPlans.stream()
                .map(plan -> new String[]{
                        plan.planName,
                        plan.provider,
                        String.format(Locale.ENGLISH, "%.1f", plan.expectedReturn),
                        plan.riskLevel,
                        String.format(Locale.ENGLISH, "%.0f", plan.minAmount)
                })
                .collect(Collectors.toList());
    }

    public List<String[]> getGovernmentSchemes() {
        return governmentSchemes.stream()
                .map(scheme -> new String[]{scheme.name, scheme.authority, scheme.benefit, scheme.eligibility})
                .collect(Collectors.toList());
    }

    public List<String[]> getRecommendations() {
        return recommendations.stream()
                .map(rec -> new String[]{rec.title, rec.rationale, rec.priority})
                .collect(Collectors.toList());
    }

    public List<String[]> getDiscussions() {
        return discussions.stream()
                .map(discussion -> new String[]{
                        discussion.id,
                        discussion.title,
                        discussion.author,
                        String.valueOf(discussion.comments.size())
                })
                .collect(Collectors.toList());
    }

    public ConsoleDiscussion getDiscussionById(String id) {
        return discussions.stream()
                .filter(discussion -> discussion.id.equalsIgnoreCase(id))
                .findFirst()
                .orElse(discussions.get(0));
    }

    public void addComment(String discussionId, String author, String content) {
        ConsoleDiscussion discussion = getDiscussionById(discussionId);
        discussion.comments.add(new ConsoleComment(author, LocalDateTime.now(), content));
    }

    public void addDiscussion(String title, String message, String author, String category) {
        String id = "D" + String.format(Locale.ENGLISH, "%03d", discussions.size() + 1);
        discussions.add(new ConsoleDiscussion(id, title, author, category, message, new ArrayList<>()));
    }

    public List<String[]> getRecommendationDiscussions() {
        return discussions.stream()
                .filter(discussion -> "Recommendation".equalsIgnoreCase(discussion.category))
                .map(discussion -> new String[]{
                        discussion.title,
                        discussion.author,
                        String.valueOf(discussion.comments.size())
                })
                .collect(Collectors.toList());
    }

    public List<String[]> getPendingBrokers() {
        return brokerApplications.stream()
                .filter(application -> "PENDING".equalsIgnoreCase(application.status))
                .map(application -> new String[]{
                        application.id,
                        application.name,
                        application.organisation,
                        application.appliedOn.format(DATE_FORMATTER)
                })
                .collect(Collectors.toList());
    }

    public String verifyBroker(String brokerId, boolean approved) {
        ConsoleBrokerApplication application = brokerApplications.stream()
                .filter(candidate -> candidate.id.equalsIgnoreCase(brokerId))
                .findFirst()
                .orElse(null);
        if (application == null) {
            return "Unknown Broker";
        }
        application.status = approved ? "APPROVED" : "REJECTED";
        return application.name;
    }

    public Object[] getPlatformStatistics() {
        return new Object[]{
                users.size(),
                1,
                transactions.size(),
                getGroupBalance(),
                brokerApplications.stream().filter(application -> "PENDING".equalsIgnoreCase(application.status)).count()
        };
    }

    public List<String[]> getSettings() {
        return settings.entrySet().stream()
                .map(entry -> new String[]{entry.getKey(), entry.getValue()})
                .collect(Collectors.toList());
    }

    public void updateSetting(String key, String value) {
        settings.put(key, value);
    }

    private boolean matchesFilter(ConsoleTransaction transaction, Integer filterChoice, String start, String end) {
        if (filterChoice == null || filterChoice == 1) {
            return true;
        }
        if (filterChoice == 2 && !"Savings".equalsIgnoreCase(transaction.type)) {
            return false;
        }
        if (filterChoice == 3 && !"Loan".equalsIgnoreCase(transaction.type)) {
            return false;
        }
        if (filterChoice == 4 && !"Expense".equalsIgnoreCase(transaction.type)) {
            return false;
        }
        if (filterChoice == 5) {
            LocalDate startDate = LocalDate.parse(start, DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(end, DATE_FORMATTER);
            return !transaction.date.isBefore(startDate) && !transaction.date.isAfter(endDate);
        }
        return true;
    }

    private void seedData() {
        users.add(new ConsoleUser("Priya Sharma", "president.priya", "password123", "President", "ACTIVE"));
        users.add(new ConsoleUser("Lakshmi Devi", "treasurer.lakshmi", "password123", "Treasurer", "ACTIVE"));
        users.add(new ConsoleUser("Meena Patel", "secretary.meena", "password123", "Secretary", "ACTIVE"));
        users.add(new ConsoleUser("Anita Rao", "member.anita", "password123", "Member", "ACTIVE"));
        users.add(new ConsoleUser("Savita Kumari", "member.savita", "password123", "Member", "ACTIVE"));
        users.add(new ConsoleUser("System Admin", "admin.root", "admin123", "Admin", "APPROVED"));

        recordTransaction("Savings", 5000, "Monthly savings", "01-03-2024", "Priya Sharma");
        recordTransaction("Loan", 10000, "Business loan - Priya", "05-03-2024", "Priya Sharma");
        recordTransaction("Expense", 1200, "Meeting expenses", "10-03-2024", "Lakshmi Devi");
        recordTransaction("Savings", 5000, "Monthly savings", "15-03-2024", "Savita Kumari");
        recordTransaction("Savings", 4500, "Monthly savings", "10-02-2024", "Anita Rao");
        recordTransaction("Loan", 8000, "Education loan", "20-02-2024", "Meena Patel");

        investmentPlans.add(new ConsoleInvestmentPlan("SBI Fixed Deposit", "SBI Bank", 8.5, "Low", "12 Months", 1000));
        investmentPlans.add(new ConsoleInvestmentPlan("Post Office MIS", "India Post", 7.4, "Low", "60 Months", 1000));
        investmentPlans.add(new ConsoleInvestmentPlan("SHG Micro Finance", "Grameen Bank", 12.0, "Medium", "24 Months", 5000));

        governmentSchemes.add(new ConsoleGovernmentScheme("DAY-NRLM", "Ministry of Rural Development",
                "Subsidised credit and capacity building", "Rural SHGs"));
        governmentSchemes.add(new ConsoleGovernmentScheme("PM Jan Dhan Yojana", "Ministry of Finance",
                "Zero-balance savings account", "All citizens"));

        recommendations.add(new ConsoleRecommendation("Increase Monthly Savings",
                "Group balance is below the six-month target threshold.", "High"));
        recommendations.add(new ConsoleRecommendation("Consider Fixed Deposit",
                "Idle funds can earn higher returns in a low-risk fixed deposit.", "Medium"));

        List<ConsoleComment> recommendationComments = new ArrayList<>();
        recommendationComments.add(new ConsoleComment("Lakshmi Devi", LocalDateTime.now().minusDays(4),
                "This is a safe option for our emergency fund."));
        discussions.add(new ConsoleDiscussion("D001", "Monthly Savings Target", "Priya Sharma", "Finance",
                "Should we increase the monthly savings target from Rs. 500 to Rs. 700?", recommendationComments));
        discussions.add(new ConsoleDiscussion("D002", "Consider Fixed Deposit", "Anita Rao", "Recommendation",
                "Let us discuss whether we should lock part of the balance in a fixed deposit.", new ArrayList<>()));

        brokerApplications.add(new ConsoleBrokerApplication("B001", "Raj Kumar", "RK Investments", LocalDate.now().minusDays(14), "PENDING"));
        brokerApplications.add(new ConsoleBrokerApplication("B002", "Sunita Joshi", "SJ Finance", LocalDate.now().minusDays(10), "PENDING"));

        settings.put("Max Loan Amount (Rs.)", "50000");
        settings.put("Min Savings Per Month", "500");
        settings.put("Loan Interest Rate (%)", "12");
    }

    public static class ConsoleDiscussion {
        public final String id;
        public final String title;
        public final String author;
        public final String category;
        public final String content;
        public final List<ConsoleComment> comments;

        public ConsoleDiscussion(String id, String title, String author, String category, String content,
                                 List<ConsoleComment> comments) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.category = category;
            this.content = content;
            this.comments = comments;
        }
    }

    public static class ConsoleComment {
        public final String author;
        public final LocalDateTime timestamp;
        public final String content;

        public ConsoleComment(String author, LocalDateTime timestamp, String content) {
            this.author = author;
            this.timestamp = timestamp;
            this.content = content;
        }
    }

    public static class ConsoleUser {
        public final String fullName;
        public final String username;
        public final String password;
        public final String role;
        public final String status;
        public double savings;
        public double loans;

        public ConsoleUser(String fullName, String username, String password, String role, String status) {
            this.fullName = fullName;
            this.username = username;
            this.password = password;
            this.role = role;
            this.status = status;
        }
    }

    private static class ConsoleTransaction {
        private final String type;
        private final double amount;
        private final String description;
        private final LocalDate date;
        private final String recordedBy;
        private final String member;

        private ConsoleTransaction(String type, double amount, String description, LocalDate date, String recordedBy, String member) {
            this.type = type;
            this.amount = amount;
            this.description = description;
            this.date = date;
            this.recordedBy = recordedBy;
            this.member = member;
        }
    }

    private static class ConsoleInvestmentPlan {
        private final String planName;
        private final String provider;
        private final double expectedReturn;
        private final String riskLevel;
        private final String tenure;
        private final double minAmount;

        private ConsoleInvestmentPlan(String planName, String provider, double expectedReturn,
                                      String riskLevel, String tenure, double minAmount) {
            this.planName = planName;
            this.provider = provider;
            this.expectedReturn = expectedReturn;
            this.riskLevel = riskLevel;
            this.tenure = tenure;
            this.minAmount = minAmount;
        }
    }

    private static class ConsoleGovernmentScheme {
        private final String name;
        private final String authority;
        private final String benefit;
        private final String eligibility;

        private ConsoleGovernmentScheme(String name, String authority, String benefit, String eligibility) {
            this.name = name;
            this.authority = authority;
            this.benefit = benefit;
            this.eligibility = eligibility;
        }
    }

    private static class ConsoleRecommendation {
        private final String title;
        private final String rationale;
        private final String priority;

        private ConsoleRecommendation(String title, String rationale, String priority) {
            this.title = title;
            this.rationale = rationale;
            this.priority = priority;
        }
    }

    private static class ConsoleBrokerApplication {
        private final String id;
        private final String name;
        private final String organisation;
        private final LocalDate appliedOn;
        private String status;

        private ConsoleBrokerApplication(String id, String name, String organisation, LocalDate appliedOn, String status) {
            this.id = id;
            this.name = name;
            this.organisation = organisation;
            this.appliedOn = appliedOn;
            this.status = status;
        }
    }
}
