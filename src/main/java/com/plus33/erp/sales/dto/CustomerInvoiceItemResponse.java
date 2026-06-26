package com.plus33.erp.sales.dto;

import java.math.BigDecimal;

public record CustomerInvoiceItemResponse(
        Long id,
        Long salesOrderItemId,
        Long pickListItemId,
        Long productId,
        String productName,
        String productSku,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal discountPercentage,
        BigDecimal taxPercentage,
        BigDecimal netAmount,
        BigDecimal taxAmount,
        BigDecimal discountAmount,
        BigDecimal totalAmount,
        Long version
) {}
