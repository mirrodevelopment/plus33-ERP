package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetImpairmentRequest(
    LocalDate impairmentDate,
    BigDecimal impairmentAmount,
    BigDecimal recoverableAmount,
    String reason
) {}
