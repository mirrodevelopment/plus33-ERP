package com.plus33.erp.sales.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

public record CompletePickingRequest(
    @NotEmpty(message = "Picking items list cannot be empty")
    List<@Valid ItemPickingUpdate> items
) {
    public record ItemPickingUpdate(
        @NotNull(message = "Product ID is required")
        Long productId,

        @NotNull(message = "Picked quantity is required")
        @PositiveOrZero(message = "Picked quantity must be greater than or equal to zero")
        BigDecimal pickedQuantity
    ) {}
}
