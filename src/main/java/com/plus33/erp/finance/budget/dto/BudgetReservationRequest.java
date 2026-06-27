package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetReservationRequest(
    Long accountId,
    BudgetDimensionSetRequest dimensionSet,
    LocalDate transactionDate,
    BigDecimal amount,
    String sourceModule,
    Long sourceReferenceId,
    String referenceNumber,
    LocalDate expiryDate
) {}
