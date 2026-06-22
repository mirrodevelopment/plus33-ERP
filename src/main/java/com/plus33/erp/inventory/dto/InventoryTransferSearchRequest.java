package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.InventoryTransferStatus;
import lombok.Builder;
import java.time.LocalDate;
import java.util.UUID;

@Builder
public record InventoryTransferSearchRequest(
        InventoryTransferStatus status,
        Long companyId,
        Long sourceWarehouseId,
        Long sourceStoreId,
        Long destWarehouseId,
        Long destStoreId,
        String transferNumber,
        UUID clientReferenceId,
        LocalDate createdAtFrom,
        LocalDate createdAtTo,
        Long createdBy,
        Long productId
) {}
