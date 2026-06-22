package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.InventoryRecallStatus;
import java.time.LocalDateTime;

public record InventoryRecallResponse(
        Long id,
        Long companyId,
        Long productId,
        Long lotId,
        String lotNumber,
        Long serialId,
        String serialNumber,
        String recallNumber,
        String recallReason,
        String recallReferenceNumber,
        InventoryRecallStatus status,
        Long recalledById,
        LocalDateTime recalledAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
