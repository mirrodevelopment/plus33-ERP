package com.plus33.erp.sales.dto;

import java.math.BigDecimal;

public record SalesOrderItemResponse(
    Long id,
    Long productId,
    String productCode,
    String productName,
    BigDecimal orderedQuantity,
    BigDecimal unitPrice,
    BigDecimal discountPercentage,
    BigDecimal taxPercentage,
    BigDecimal lineTotal,
    Long version
) {}
