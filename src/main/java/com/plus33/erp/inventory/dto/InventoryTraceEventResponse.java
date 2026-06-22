package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.InventoryTraceEventType;
import com.plus33.erp.inventory.entity.InventoryTraceReferenceType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventoryTraceEventResponse(
        Long id,
        Long companyId,
        Long productId,
        Long lotId,
        String lotNumber,
        Long serialId,
        String serialNumber,
        Long warehouseId,
        Long storeId,
        InventoryTraceEventType eventType,
        BigDecimal quantity,
        InventoryTraceReferenceType referenceType,
        Long referenceId,
        String referenceNumber,
        String notes,
        Long createdById,
        LocalDateTime createdAt
) {}
