package com.plus33.erp.inventory.dto;

import java.math.BigDecimal;

public record StockCountItemResponse(
        Long id,
        Long productId,
        String productCode,
        String productName,
        BigDecimal systemQuantity,
        BigDecimal reservedQuantity,
        BigDecimal availableQuantity,
        BigDecimal countedQuantity,
        BigDecimal variance,
        Long version
) {}
