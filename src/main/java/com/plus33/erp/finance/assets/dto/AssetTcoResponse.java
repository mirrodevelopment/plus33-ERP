package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;

public record AssetTcoResponse(
    Long fixedAssetId,
    String assetCode,
    String assetName,
    BigDecimal acquisitionCost,
    BigDecimal capitalizedMaintenance,
    BigDecimal operatingMaintenance,
    BigDecimal totalDepreciation,
    BigDecimal totalInsurance,
    BigDecimal totalDowntimeCost,
    BigDecimal totalCostOfOwnership,
    BigDecimal costPerOperatingHour,
    BigDecimal costPerUnit,
    Integer totalDowntimeHours,
    Integer totalOperatingHours
) {}
