package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;

public record AssetCategoryRequest(
    String code,
    String name,
    String depreciationMethod, // STRAIGHT_LINE, WDV, NONE
    BigDecimal depreciationRate,
    Integer usefulLifeYears,
    Long assetAccountId,
    Long accumulatedDepreciationAccountId,
    Long depreciationExpenseAccountId,
    Long gainLossAccountId
) {}
