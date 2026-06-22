package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.InventoryAdjustmentStatus;
import com.plus33.erp.inventory.entity.InventoryAdjustmentType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record InventoryAdjustmentResponse(
        Long id,
        String adjustmentNumber,
        Long companyId,
        Long warehouseId,
        Long storeId,
        InventoryAdjustmentType adjustmentType,
        InventoryAdjustmentStatus status,
        UUID clientReferenceId,
        String remarks,
        Long createdById,
        LocalDateTime createdAt,
        Long submittedById,
        LocalDateTime submittedAt,
        Long approvedById,
        LocalDateTime approvedAt,
        Long postedById,
        LocalDateTime postedAt,
        Long cancelledById,
        LocalDateTime cancelledAt,
        String cancellationReason,
        List<InventoryAdjustmentItemResponse> items,
        Long version
) {}
