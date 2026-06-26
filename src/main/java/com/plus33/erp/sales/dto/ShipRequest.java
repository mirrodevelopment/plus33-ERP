package com.plus33.erp.sales.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

public record ShipRequest(
    @NotEmpty(message = "Shipment items list cannot be empty")
    List<@Valid ItemShipmentUpdate> items
) {
    public record ItemShipmentUpdate(
        @NotNull(message = "Product ID is required")
        Long productId,

        @NotNull(message = "Shipped quantity is required")
        @PositiveOrZero(message = "Shipped quantity must be greater than or equal to zero")
        BigDecimal shippedQuantity
    ) {}
}
