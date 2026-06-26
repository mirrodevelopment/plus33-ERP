package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetPartialDisposalRequest(
    BigDecimal disposalAmount,
    LocalDate disposalDate,
    BigDecimal saleProceeds,
    String reason
) {}
