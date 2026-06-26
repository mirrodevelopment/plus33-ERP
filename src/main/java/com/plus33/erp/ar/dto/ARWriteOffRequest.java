package com.plus33.erp.ar.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request payload for recording a bad-debt write-off.
 */
public record ARWriteOffRequest(

        @NotNull(message = "Company ID is required")
        Long companyId,

        @NotNull(message = "Customer Invoice ID is required")
        Long customerInvoiceId,

        @NotNull(message = "Write-off amount is required")
        @Positive(message = "Write-off amount must be greater than zero")
        BigDecimal writeOffAmount,

        @NotNull(message = "Write-off date is required")
        LocalDate writeOffDate,

        String reason
) {}
