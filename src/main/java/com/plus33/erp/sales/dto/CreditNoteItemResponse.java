package com.plus33.erp.sales.dto;

import java.math.BigDecimal;

public record CreditNoteItemResponse(
    Long id,
    Long productId,
    String productName,
    String productCode,
    BigDecimal quantity,
    BigDecimal unitPrice,
    BigDecimal discountPercentage,
    BigDecimal taxPercentage,
    BigDecimal netAmount,
    BigDecimal taxAmount,
    BigDecimal discountAmount,
    BigDecimal totalAmount
) {}
