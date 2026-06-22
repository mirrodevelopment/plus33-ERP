package com.plus33.erp.inventory.dto;

import java.math.BigDecimal;

public record InventoryTransferItemResponse(
        Long id,
        Long productId,
        String productName,
        BigDecimal quantity,
        BigDecimal receivedQuantity,
        BigDecimal remainingQuantity
) {}
