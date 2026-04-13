# OOAD Patterns Used

This project now includes explicit OOAD design principles and patterns in the working application flow.

## Design Principles

- `Single Responsibility Principle`
  Controllers handle HTTP, facades coordinate workflows, factories create domain objects, adapters shape API payloads, and repositories manage persistence.
- `Dependency Inversion Principle`
  Web controllers depend on facade interfaces such as `FinanceFacade` and `AdvisoryFacade` instead of concrete workflow implementations.
- `Open/Closed Principle`
  New payload adapters or factory implementations can be introduced without changing controller logic.

## Creational Patterns

- `Factory`
  `FinancialRecordFactory` and `DefaultFinancialRecordFactory` centralize creation of `Transaction` and `MonthlyReport`.
- `Builder`
  `TransactionBuilder` and `MonthlyReportBuilder` create domain objects step by step.

## Structural Patterns

- `Facade`
  `FinanceFacadeService` simplifies finance and reporting workflows for `FinanceApiController`.
  `AdvisoryFacadeService` simplifies advisory workflows for `AdvisoryApiController`.
- `Adapter`
  `InvestmentPlanPayloadAdapter`, `GovernmentSchemePayloadAdapter`, and `RecommendationPayloadAdapter` convert domain models into API response payloads.

## Existing Architectural Pattern

- `Repository`
  Spring Data JPA repositories provide persistence abstraction across the project.
