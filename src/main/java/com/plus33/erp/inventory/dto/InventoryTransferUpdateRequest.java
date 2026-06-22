package com.plus33.erp.inventory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record InventoryTransferUpdateRequest(
        Long sourceWarehouseId,
        Long sourceStoreId,
        Long destWarehouseId,
        Long destStoreId,
        String remarks,

        @NotEmpty(message = "Transfer must contain at least one item")
        @Valid
        List<InventoryTransferItemRequest> items
) {}
