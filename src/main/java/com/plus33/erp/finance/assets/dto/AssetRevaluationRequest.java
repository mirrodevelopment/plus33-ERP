package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetRevaluationRequest(
    LocalDate revaluationDate,
    BigDecimal newFairValue,
    Long revaluationReserveAccountId,
    String reason
) {}
