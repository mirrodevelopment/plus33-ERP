package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.InventorySerialStatus;
import java.time.LocalDateTime;

public record InventorySerialResponse(
        Long id,
        Long companyId,
        Long productId,
        Long lotId,
        String lotNumber,
        String serialNumber,
        Long warehouseId,
        Long storeId,
        InventorySerialStatus status,
        Long version,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
