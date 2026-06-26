package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetDisposalRequest(
    LocalDate disposalDate,
    String disposalType, // DISPOSED, WRITTEN_OFF
    BigDecimal proceeds,
    String reason
) {}
