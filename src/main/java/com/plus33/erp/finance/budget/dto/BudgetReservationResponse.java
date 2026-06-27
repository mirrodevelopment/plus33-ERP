package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record BudgetReservationResponse(
    Long id,
    Long budgetLineId,
    String sourceModule,
    Long sourceReferenceId,
    String referenceNumber,
    BigDecimal reservedAmount,
    String status,
    LocalDate expiryDate,
    LocalDateTime createdAt
) {}
