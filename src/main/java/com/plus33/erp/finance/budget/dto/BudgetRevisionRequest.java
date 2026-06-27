package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetRevisionRequest(
    Long budgetLineId,
    LocalDate revisionDate,
    BigDecimal newAmount,
    String reason
) {}
