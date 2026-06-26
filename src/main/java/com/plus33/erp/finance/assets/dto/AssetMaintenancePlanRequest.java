package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetMaintenancePlanRequest(
    Long fixedAssetId,
    String planName,
    String frequency,
    LocalDate nextDueDate,
    BigDecimal estimatedCost,
    String assignedVendor,
    String notes
) {}
