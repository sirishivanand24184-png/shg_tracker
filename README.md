# SHG Financial Tracker

A Java-based SHG Financial Tracking and Advisory Platform for managing savings, loans, expenses, reports, recommendations, discussions, and admin workflows.

This project now supports:
- a Spring Boot web application with HTML pages and REST APIs
- a Java console application for menu-driven demonstration
- seed/demo data for easy evaluation
- embedded H2 database by default, with optional MySQL configuration

## Overview

The system is designed for Self-Help Groups (SHGs) to:
- maintain transparent financial records
- track savings, loans, and expenses
- view monthly and comparative reports
- access investment plans and government schemes
- discuss decisions collectively
- manage members and broker verification workflows

## Implemented Features

- Financial tracking
  Record and view transactions for savings, loans, and expenses.
- Reports
  Generate monthly summaries and multi-month comparisons.
- Advisory
  View investment plans, government schemes, and recommendations.
- Discussions
  Create discussion threads and add comments.
- Admin support
  View platform statistics, broker verification data, and settings.
- Authentication
  Login and registration APIs for the web UI.
- Console mode
  Interactive menu-driven version via `ApplicationNavigator`.

## Tech Stack

- Java 11
- Spring Boot 2.7
- Spring MVC
- Spring Data JPA
- Thymeleaf
- H2 Database
- Maven
- JUnit 5

## Run The Project

### Option 1: Run the Web UI

```bash
mvn spring-boot:run
```

Then open:
- `http://localhost:8080/`

Useful pages:
- `http://localhost:8080/dashboard`
- `http://localhost:8080/finance/transactions`
- `http://localhost:8080/finance/reports`
- `http://localhost:8080/advisory/investments`
- `http://localhost:8080/advisory/schemes`
- `http://localhost:8080/advisory/recommendations`
- `http://localhost:8080/discussion/list`
- `http://localhost:8080/admin/statistics`

### Option 2: Run the Console UI

Run the main method in:
- `src/main/java/com/shg/view/ApplicationNavigator.java`

Or configure your IDE to run `com.shg.view.ApplicationNavigator`.

## Test The Project

```bash
mvn test
```

## Default Login Credentials

Seed data is loaded automatically at startup.

Use these sample logins:
- President
  username: `president.priya`
  password: `password123`
- Admin
  username: `admin.root`
  password: `admin123`
- Treasurer
  username: `treasurer.lakshmi`
  password: `password123`

## Database Configuration

By default the application runs on embedded H2, so no external database setup is required.

Configuration file:
- `src/main/resources/application.properties`

To use MySQL instead, set:
- `SHG_DB_URL`
- `SHG_DB_USERNAME`
- `SHG_DB_PASSWORD`
- `SHG_DB_DRIVER_CLASS_NAME`
- `SHG_JPA_DIALECT`

## Project Structure

```text
shg-financial-tracker/
+-- pom.xml
+-- README.md
+-- .gitignore
+-- src
¦   +-- main
¦   ¦   +-- java
¦   ¦   ¦   +-- com
¦   ¦   ¦       +-- shg
¦   ¦   ¦           +-- DemoDataLoader.java
¦   ¦   ¦           +-- SHGFinancialTrackerApplication.java
¦   ¦   ¦           +-- WebController.java
¦   ¦   ¦           +-- controller
¦   ¦   ¦           ¦   +-- AdminApiController.java
¦   ¦   ¦           ¦   +-- AdvisoryApiController.java
¦   ¦   ¦           ¦   +-- AuthApiController.java
¦   ¦   ¦           ¦   +-- DashboardApiController.java
¦   ¦   ¦           ¦   +-- DiscussionApiController.java
¦   ¦   ¦           ¦   +-- FinanceApiController.java
¦   ¦   ¦           ¦   +-- MemberApiController.java
¦   ¦   ¦           ¦   +-- MonthlyReportController.java
¦   ¦   ¦           ¦   +-- SHGGroupController.java
¦   ¦   ¦           ¦   +-- SHGMemberController.java
¦   ¦   ¦           ¦   +-- TransactionController.java
¦   ¦   ¦           +-- model
¦   ¦   ¦           ¦   +-- Comment.java
¦   ¦   ¦           ¦   +-- Discussion.java
¦   ¦   ¦           ¦   +-- GovernmentScheme.java
¦   ¦   ¦           ¦   +-- InvestmentPlan.java
¦   ¦   ¦           ¦   +-- MonthlyReport.java
¦   ¦   ¦           ¦   +-- Recommendation.java
¦   ¦   ¦           ¦   +-- SHGGroup.java
¦   ¦   ¦           ¦   +-- SHGMember.java
¦   ¦   ¦           ¦   +-- Transaction.java
¦   ¦   ¦           +-- repository
¦   ¦   ¦           ¦   +-- CommentRepository.java
¦   ¦   ¦           ¦   +-- DiscussionRepository.java
¦   ¦   ¦           ¦   +-- GovernmentSchemeRepository.java
¦   ¦   ¦           ¦   +-- InvestmentPlanRepository.java
¦   ¦   ¦           ¦   +-- MonthlyReportRepository.java
¦   ¦   ¦           ¦   +-- RecommendationRepository.java
¦   ¦   ¦           ¦   +-- SHGGroupRepository.java
¦   ¦   ¦           ¦   +-- SHGMemberRepository.java
¦   ¦   ¦           ¦   +-- TransactionRepository.java
¦   ¦   ¦           +-- service
¦   ¦   ¦           ¦   +-- AdminService.java
¦   ¦   ¦           ¦   +-- AdvisoryService.java
¦   ¦   ¦           ¦   +-- AppSettingsService.java
¦   ¦   ¦           ¦   +-- ConsolePlatformService.java
¦   ¦   ¦           ¦   +-- DashboardService.java
¦   ¦   ¦           ¦   +-- DiscussionService.java
¦   ¦   ¦           ¦   +-- MonthlyReportService.java
¦   ¦   ¦           ¦   +-- SHGGroupService.java
¦   ¦   ¦           ¦   +-- SHGMemberService.java
¦   ¦   ¦           ¦   +-- TransactionService.java
¦   ¦   ¦           +-- view
¦   ¦   ¦               +-- AdminView.java
¦   ¦   ¦               +-- AdvisoryView.java
¦   ¦   ¦               +-- ApplicationNavigator.java
¦   ¦   ¦               +-- DashboardView.java
¦   ¦   ¦               +-- DiscussionView.java
¦   ¦   ¦               +-- FinanceView.java
¦   ¦   ¦               +-- LoginView.java
¦   ¦   ¦               +-- ReportView.java
¦   ¦   ¦               +-- UIUtility.java
¦   ¦   +-- resources
¦   ¦       +-- application.properties
¦   ¦       +-- sql
¦   ¦       ¦   +-- schema.sql
¦   ¦       +-- static
¦   ¦       ¦   +-- css
¦   ¦       ¦   ¦   +-- bootstrap.min.css
¦   ¦       ¦   ¦   +-- responsive.css
¦   ¦       ¦   ¦   +-- style.css
¦   ¦       ¦   ¦   +-- theme.css
¦   ¦       ¦   +-- js
¦   ¦       ¦       +-- admin.js
¦   ¦       ¦       +-- advisory.js
¦   ¦       ¦       +-- api.js
¦   ¦       ¦       +-- app.js
¦   ¦       ¦       +-- auth.js
¦   ¦       ¦       +-- dashboard.js
¦   ¦       ¦       +-- discussion.js
¦   ¦       ¦       +-- finance.js
¦   ¦       ¦       +-- utils.js
¦   ¦       +-- templates
¦   ¦           +-- admin
¦   ¦           ¦   +-- brokers.html
¦   ¦           ¦   +-- settings.html
¦   ¦           ¦   +-- statistics.html
¦   ¦           +-- advisory
¦   ¦           ¦   +-- discussion.html
¦   ¦           ¦   +-- investments.html
¦   ¦           ¦   +-- recommendations.html
¦   ¦           ¦   +-- schemes.html
¦   ¦           +-- discussion
¦   ¦           ¦   +-- detail.html
¦   ¦           ¦   +-- list.html
¦   ¦           +-- finance
¦   ¦           ¦   +-- analytics.html
¦   ¦           ¦   +-- reports.html
¦   ¦           ¦   +-- transactions.html
¦   ¦           +-- admin.html
¦   ¦           +-- dashboard.html
¦   ¦           +-- error.html
¦   ¦           +-- group.html
¦   ¦           +-- index.html
¦   ¦           +-- members.html
¦   ¦           +-- reports.html
¦   ¦           +-- transaction.html
¦   +-- test
¦       +-- java
¦           +-- com
¦               +-- shg
¦                   +-- SHGFinancialTrackerApplicationTests.java
```

## Important Notes

- Web routes are served through Thymeleaf templates.
- REST APIs used by the frontend are under `/api/...`.
- Demo data is auto-loaded on startup.
- The app is ready to demonstrate without setting up MySQL.

## Submission Status

- Backend wired and runnable
- Frontend pages connected to REST APIs
- Console flow retained
- Seed data added
- Tests passing with `mvn test`

## License

This project is intended for academic and educational use.
