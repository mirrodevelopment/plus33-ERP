package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetUtilizationRequest(
    Long fixedAssetId,
    LocalDate recordDate,
    BigDecimal hoursUsed,
    BigDecimal outputUnits,
    String notes
) {}
