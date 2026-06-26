package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;

public record DepreciationPreviewEntry(
    Long assetId,
    String assetCode,
    String assetName,
    BigDecimal originalCost,
    BigDecimal bookValueBefore,
    BigDecimal depreciationAmount,
    BigDecimal bookValueAfter
) {}
