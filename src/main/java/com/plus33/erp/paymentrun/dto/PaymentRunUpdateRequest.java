package com.plus33.erp.paymentrun.dto;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

public record PaymentRunUpdateRequest(
        LocalDate paymentDate,
        String paymentMethod,
        String bankAccountCode,
        String exportFormat,
        @Valid
        List<PaymentRunInvoiceRequest> invoices
) {}
