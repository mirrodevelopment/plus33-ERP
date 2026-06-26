package com.plus33.erp.finance.reporting.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TrialBalanceResponse(
    List<TrialBalanceEntry> entries,
    LocalDate startDate,
    LocalDate endDate,
    BigDecimal totalDebits,
    BigDecimal totalCredits,
    BigDecimal difference,
    boolean balanced,
    String validationMessage,
    List<ReportWarning> warnings,
    int numberOfAccounts,
    LocalDateTime generatedAt,
    String generatedBy
) {}
