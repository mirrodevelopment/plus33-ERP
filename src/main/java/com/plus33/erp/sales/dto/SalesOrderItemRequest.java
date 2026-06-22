package com.plus33.erp.sales.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SalesOrderItemRequest(
    @NotNull(message = "Product ID is required")
    Long productId,

    @NotNull(message = "Ordered quantity is required")
    @DecimalMin(value = "0.01", message = "Ordered quantity must be greater than 0")
    BigDecimal orderedQuantity,

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", message = "Unit price cannot be negative")
    BigDecimal unitPrice,

    @DecimalMin(value = "0.0", message = "Discount percentage cannot be negative")
    @DecimalMax(value = "100.0", message = "Discount percentage cannot exceed 100%")
    BigDecimal discountPercentage,

    @DecimalMin(value = "0.0", message = "Tax percentage cannot be negative")
    @DecimalMax(value = "100.0", message = "Tax percentage cannot exceed 100%")
    BigDecimal taxPercentage
) {}
