package com.plus33.erp.inventory.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record ReplenishmentRuleRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        @NotNull(message = "Product ID is required")
        Long productId,

        Long warehouseId,
        Long storeId,

        @NotNull(message = "Min quantity is required")
        @DecimalMin(value = "0.00", message = "Min quantity must be at least 0")
        BigDecimal minQuantity,

        @NotNull(message = "Max quantity is required")
        @DecimalMin(value = "0.01", message = "Max quantity must be greater than 0")
        BigDecimal maxQuantity,

        @NotNull(message = "Reorder point is required")
        @DecimalMin(value = "0.00", message = "Reorder point must be at least 0")
        BigDecimal reorderPoint,

        @NotNull(message = "Reorder quantity is required")
        @DecimalMin(value = "0.01", message = "Reorder quantity must be greater than 0")
        BigDecimal reorderQuantity,

        @Min(value = 0, message = "Lead time days must be at least 0")
        Integer leadTimeDays,

        Boolean active,

        @NotNull(message = "Client reference ID is required")
        UUID clientReferenceId
) {}
