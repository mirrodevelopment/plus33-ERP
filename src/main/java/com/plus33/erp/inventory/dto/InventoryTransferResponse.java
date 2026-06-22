package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.InventoryTransferStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record InventoryTransferResponse(
        Long id,
        String transferNumber,
        Long companyId,
        Long sourceWarehouseId,
        Long sourceStoreId,
        Long destWarehouseId,
        Long destStoreId,
        InventoryTransferStatus status,
        UUID clientReferenceId,
        String remarks,
        Long createdById,
        LocalDateTime createdAt,
        Long submittedById,
        LocalDateTime submittedAt,
        Long approvedById,
        LocalDateTime approvedAt,
        Long dispatchedById,
        LocalDateTime dispatchedAt,
        Long receivedById,
        LocalDateTime receivedAt,
        Long cancelledById,
        LocalDateTime cancelledAt,
        String cancellationReason,
        List<InventoryTransferItemResponse> items,
        Long version
) {}
