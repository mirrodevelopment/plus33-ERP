package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.InventoryLotStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record InventoryLotResponse(
        Long id,
        Long companyId,
        Long productId,
        String lotNumber,
        LocalDate expiryDate,
        LocalDate manufacturedDate,
        InventoryLotStatus status,
        Long version,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
