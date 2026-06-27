package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record BudgetLineResponse(
    Long id,
    Long budgetId,
    Long budgetVersionId,
    Long accountId,
    String accountCode,
    String accountName,
    BudgetDimensionSetResponse dimensionSet,
    LocalDate periodStartDate,
    LocalDate periodEndDate,
    BigDecimal allocatedAmount,
    BigDecimal reservedAmount,
    BigDecimal consumedAmount,
    BigDecimal availableAmount,
    Boolean isLocked,
    String distributionMethod,
    String formulaExpression,
    BigDecimal forecastConfidence,
    BigDecimal predictedSpend,
    BigDecimal predictedRevenue,
    String aiRecommendation,
    LocalDateTime aiGeneratedAt,
    String notes
) {}
