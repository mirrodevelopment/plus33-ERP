package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.InventoryAdjustmentStatus;
import com.plus33.erp.inventory.entity.InventoryAdjustmentType;
import lombok.Builder;
import java.time.LocalDate;
import java.util.UUID;

@Builder
public record InventoryAdjustmentSearchRequest(
        InventoryAdjustmentStatus status,
        Long companyId,
        Long warehouseId,
        Long storeId,
        InventoryAdjustmentType adjustmentType,
        String adjustmentNumber,
        UUID clientReferenceId,
        LocalDate createdAtFrom,
        LocalDate createdAtTo,
        Long createdBy,
        Long productId
) {}
