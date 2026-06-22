package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.InventoryAdjustmentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record InventoryAdjustmentUpdateRequest(
        Long warehouseId,
        Long storeId,

        @NotNull(message = "Adjustment type is required")
        InventoryAdjustmentType adjustmentType,

        String remarks,

        @NotEmpty(message = "Adjustment must contain at least one item")
        @Valid
        List<InventoryAdjustmentItemRequest> items
) {}
