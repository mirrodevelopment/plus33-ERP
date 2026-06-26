package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetImpairmentResponse(
    Long id,
    Long fixedAssetId,
    LocalDate impairmentDate,
    BigDecimal impairmentAmount,
    BigDecimal recoverableAmount,
    Long journalEntryId,
    String reason,
    String performedBy
) {}
