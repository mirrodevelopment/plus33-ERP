package com.plus33.erp.finance.reporting.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record BalanceSheetResponse(
    List<BalanceSheetEntry> assets,
    List<BalanceSheetEntry> liabilities,
    List<BalanceSheetEntry> equities,
    BigDecimal totalAssets,
    BigDecimal totalLiabilities,
    BigDecimal totalEquity,
    BigDecimal difference,
    boolean balanced,
    String validationMessage,
    LocalDate asOfDate,
    LocalDateTime generatedAt,
    String generatedBy
) {}
