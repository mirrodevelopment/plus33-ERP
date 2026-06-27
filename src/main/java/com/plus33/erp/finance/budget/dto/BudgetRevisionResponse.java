package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetRevisionResponse(
    Long id,
    Long budgetId,
    Long budgetLineId,
    LocalDate revisionDate,
    BigDecimal previousAmount,
    BigDecimal newAmount,
    BigDecimal changeAmount,
    String reason,
    String performedBy,
    String status
) {}
