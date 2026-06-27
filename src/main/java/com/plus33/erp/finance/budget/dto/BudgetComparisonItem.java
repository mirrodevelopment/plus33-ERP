package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;

public record BudgetComparisonItem(
    Long accountId,
    String accountCode,
    String accountName,
    BudgetDimensionSetResponse dimensionSet,
    BigDecimal allocatedAmount1,
    BigDecimal reservedAmount1,
    BigDecimal consumedAmount1,
    BigDecimal availableAmount1,
    BigDecimal allocatedAmount2,
    BigDecimal reservedAmount2,
    BigDecimal consumedAmount2,
    BigDecimal availableAmount2,
    BigDecimal varianceAmount,
    BigDecimal variancePercent
) {}
