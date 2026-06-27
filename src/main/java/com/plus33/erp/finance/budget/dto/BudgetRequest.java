package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.util.List;

public record BudgetRequest(
    Long fiscalYearId,
    Long budgetPolicyId,
    Long workflowTemplateId,
    String code,
    String name,
    String budgetType, // OPERATING, CAPITAL
    String periodType, // ANNUAL, QUARTERLY, MONTHLY, WEEKLY, CUSTOM
    String scenario, // EXPECTED, BEST_CASE, WORST_CASE
    Boolean isForecast,
    String forecastType,
    String forecastCycleCode,
    String rateLockType, // HISTORICAL, BUDGET_RATE, MONTHLY_AVERAGE, SPOT
    BigDecimal budgetExchangeRate,
    List<BudgetLineRequest> lines
) {}
