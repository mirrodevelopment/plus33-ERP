package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;

public record AssetCategoryResponse(
    Long id,
    String code,
    String name,
    String depreciationMethod,
    BigDecimal depreciationRate,
    Integer usefulLifeYears,
    Long assetAccountId,
    String assetAccountCode,
    String assetAccountName,
    Long accumulatedDepreciationAccountId,
    String accumulatedDepreciationAccountCode,
    Long depreciationExpenseAccountId,
    String depreciationExpenseAccountCode,
    Long gainLossAccountId,
    String gainLossAccountCode
) {}
