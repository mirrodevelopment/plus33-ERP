package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetRevaluationResponse(
    Long id,
    Long fixedAssetId,
    LocalDate revaluationDate,
    BigDecimal previousValue,
    BigDecimal newFairValue,
    Long revaluationReserveAccountId,
    Long journalEntryId,
    String reason,
    String performedBy
) {}
