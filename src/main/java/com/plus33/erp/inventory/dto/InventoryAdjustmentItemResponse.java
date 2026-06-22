package com.plus33.erp.inventory.dto;

import java.math.BigDecimal;

public record InventoryAdjustmentItemResponse(
        Long id,
        Long productId,
        String productName,
        BigDecimal quantity
) {}
