package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.InventoryAdjustmentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record InventoryAdjustmentRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        Long warehouseId,
        Long storeId,

        @NotNull(message = "Adjustment type is required")
        InventoryAdjustmentType adjustmentType,

        @NotNull(message = "Client reference ID is required")
        UUID clientReferenceId,

        String remarks,

        @NotEmpty(message = "Adjustment must contain at least one item")
        @Valid
        List<InventoryAdjustmentItemRequest> items
) {}
