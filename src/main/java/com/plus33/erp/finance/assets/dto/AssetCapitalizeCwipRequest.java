package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetCapitalizeCwipRequest(
    LocalDate capitalizationDate,
    BigDecimal totalCapitalizedCost,
    Long assetCategoryId,
    Integer usefulLifeYears,
    BigDecimal salvageValue,
    String depreciationMethod
) {}
