package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BudgetConsumptionResponse(
    Long id,
    Long budgetLineId,
    String sourceModule,
    Long sourceReferenceId,
    String referenceNumber,
    BigDecimal consumedAmount,
    LocalDateTime createdAt
) {}
