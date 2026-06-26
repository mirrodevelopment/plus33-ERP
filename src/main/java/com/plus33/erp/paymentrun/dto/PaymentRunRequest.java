package com.plus33.erp.paymentrun.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record PaymentRunRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        @NotNull(message = "Payment date is required")
        LocalDate paymentDate,

        @NotBlank(message = "Payment method is required")
        String paymentMethod,

        @NotBlank(message = "Currency code is required")
        String currencyCode,

        String bankAccountCode,

        LocalDate filterDueDate,

        Long filterSupplierId,

        String exportFormat,

        UUID clientReferenceId
) {}

