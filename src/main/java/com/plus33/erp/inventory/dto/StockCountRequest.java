package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.StockCountType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record StockCountRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        Long warehouseId,
        Long storeId,

        @NotNull(message = "Count type is required")
        StockCountType countType,

        Boolean blindCount,

        @DecimalMin(value = "0.00", message = "Approval threshold percentage must be at least 0%")
        @DecimalMax(value = "100.00", message = "Approval threshold percentage cannot exceed 100%")
        BigDecimal approvalThresholdPercentage,

        @NotNull(message = "Client reference ID is required")
        UUID clientReferenceId,

        String remarks,

        List<Long> cycleProductIds
) {}
