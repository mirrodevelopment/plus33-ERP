package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;

public record InvoiceMatchingResponse(
        Long companyId,
        Long supplierInvoiceId,
        String invoiceNumber,
        Long purchaseOrderId,
        String orderNumber,
        String supplierName,
        BigDecimal invoiceTotalAmount,
        BigDecimal poTotalAmount,
        Boolean hasQuantityMismatch,
        Boolean hasPriceMismatch
) {}
