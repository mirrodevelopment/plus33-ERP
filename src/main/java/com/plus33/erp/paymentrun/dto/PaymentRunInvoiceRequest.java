package com.plus33.erp.paymentrun.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PaymentRunInvoiceRequest(
        @NotNull(message = "Supplier Invoice ID is required")
        Long supplierInvoiceId,

        @NotNull(message = "Payment amount is required")
        @DecimalMin(value = "0.00", message = "Payment amount must be greater than or equal to zero")
        BigDecimal paymentAmount,

        String paymentReference
) {}
