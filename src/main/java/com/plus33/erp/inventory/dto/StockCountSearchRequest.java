package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.StockCountStatus;
import com.plus33.erp.inventory.entity.StockCountType;
import lombok.Builder;
import java.time.LocalDate;
import java.util.UUID;

@Builder
public record StockCountSearchRequest(
        StockCountStatus status,
        Long companyId,
        Long warehouseId,
        Long storeId,
        StockCountType countType,
        String countNumber,
        UUID clientReferenceId,
        LocalDate createdAtFrom,
        LocalDate createdAtTo,
        Long createdBy,
        Long assignedTo,
        Long productId
) {}
