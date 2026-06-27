package com.plus33.erp.finance.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record JournalEntryLineRequest(
        @NotNull(message = "Account ID is required")
        Long accountId,

        @NotNull(message = "Debit amount is required")
        @PositiveOrZero(message = "Debit amount must be positive or zero")
        BigDecimal debitAmount,

        @NotNull(message = "Credit amount is required")
        @PositiveOrZero(message = "Credit amount must be positive or zero")
        BigDecimal creditAmount,

        Long dimensionSetId
) {}
