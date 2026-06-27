package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetLineRequest(
    Long accountId,
    BudgetDimensionSetRequest dimensionSet,
    LocalDate periodStartDate,
    LocalDate periodEndDate,
    BigDecimal allocatedAmount,
    String distributionMethod, // EQUAL, MANUAL, SEASONAL, PREV_YEAR_ACTUAL, PERCENTAGE, FORMULA
    String formulaExpression,
    String notes
) {}
