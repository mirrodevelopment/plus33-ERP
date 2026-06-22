package com.plus33.erp.inventory.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record InventoryAdjustmentItemRequest(
        @NotNull(message = "Product ID is required")
        Long productId,

        @NotNull(message = "Quantity is required")
        BigDecimal quantity
) {}
