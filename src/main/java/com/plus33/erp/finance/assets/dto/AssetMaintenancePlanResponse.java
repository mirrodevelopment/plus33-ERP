package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetMaintenancePlanResponse(
    Long id,
    Long fixedAssetId,
    String assetCode,
    String assetName,
    String planName,
    String frequency,
    LocalDate nextDueDate,
    BigDecimal estimatedCost,
    String assignedVendor,
    String notes,
    boolean isActive
) {}
