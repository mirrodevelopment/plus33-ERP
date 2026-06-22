package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.StockCountType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record StockCountUpdateRequest(
        Long warehouseId,
        Long storeId,

        @NotNull(message = "Count type is required")
        StockCountType countType,

        Boolean blindCount,

        @DecimalMin(value = "0.00", message = "Approval threshold percentage must be at least 0%")
        @DecimalMax(value = "100.00", message = "Approval threshold percentage cannot exceed 100%")
        BigDecimal approvalThresholdPercentage,

        String remarks,

        List<Long> cycleProductIds
) {}
