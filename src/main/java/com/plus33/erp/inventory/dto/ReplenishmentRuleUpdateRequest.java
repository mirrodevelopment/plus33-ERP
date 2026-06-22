package com.plus33.erp.inventory.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

public record ReplenishmentRuleUpdateRequest(
        @DecimalMin(value = "0.00", message = "Min quantity must be at least 0")
        BigDecimal minQuantity,

        @DecimalMin(value = "0.01", message = "Max quantity must be greater than 0")
        BigDecimal maxQuantity,

        @DecimalMin(value = "0.00", message = "Reorder point must be at least 0")
        BigDecimal reorderPoint,

        @DecimalMin(value = "0.01", message = "Reorder quantity must be greater than 0")
        BigDecimal reorderQuantity,

        @Min(value = 0, message = "Lead time days must be at least 0")
        Integer leadTimeDays,

        Boolean active
) {}
