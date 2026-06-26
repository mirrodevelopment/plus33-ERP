package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;

public record AssetDisposalResponse(
    Long id,
    String status,
    BigDecimal acquisitionCost,
    BigDecimal accumulatedDepreciation,
    BigDecimal currentBookValue,
    BigDecimal proceeds,
    BigDecimal gainLossAmount,
    Long journalEntryId
) {}
