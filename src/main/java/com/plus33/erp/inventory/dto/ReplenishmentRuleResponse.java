package com.plus33.erp.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReplenishmentRuleResponse(
        Long id,
        Long companyId,
        Long productId,
        String productName,
        Long warehouseId,
        String warehouseName,
        Long storeId,
        String storeName,
        BigDecimal minQuantity,
        BigDecimal maxQuantity,
        BigDecimal reorderPoint,
        BigDecimal reorderQuantity,
        int leadTimeDays,
        boolean active,
        UUID clientReferenceId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version
) {}
