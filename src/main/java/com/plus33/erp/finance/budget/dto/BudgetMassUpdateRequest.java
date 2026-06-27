package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.util.List;

public record BudgetMassUpdateRequest(
    List<Long> budgetLineIds,
    String adjustmentType, // PERCENTAGE, FIXED_AMOUNT
    BigDecimal adjustmentValue, // e.g. 10.00 for +10% or -5.00 for -5% or fixed amount delta
    String reason
) {}
