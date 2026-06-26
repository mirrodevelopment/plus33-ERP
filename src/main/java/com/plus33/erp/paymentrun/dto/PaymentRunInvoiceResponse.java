package com.plus33.erp.paymentrun.dto;

import java.math.BigDecimal;

public record PaymentRunInvoiceResponse(
        Long id,
        Long supplierInvoiceId,
        String invoiceNumber,
        Long supplierId,
        String supplierName,
        BigDecimal invoiceOutstandingBalance,
        BigDecimal paymentAmount,
        String paymentReference
) {}
