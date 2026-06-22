package com.plus33.erp.inventory.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record StockCountItemCountRequest(
        @NotNull(message = "Product ID is required")
        Long productId,

        BigDecimal countedQuantity
) {}
