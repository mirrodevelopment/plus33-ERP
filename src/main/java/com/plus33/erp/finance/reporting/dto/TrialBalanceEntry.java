package com.plus33.erp.finance.reporting.dto;

import java.math.BigDecimal;

public record TrialBalanceEntry(
    Long accountId,
    String accountCode,
    String accountName,
    String accountType,
    Long parentAccountId,
    BigDecimal openingDebit,
    BigDecimal openingCredit,
    BigDecimal periodDebit,
    BigDecimal periodCredit,
    BigDecimal closingDebit,
    BigDecimal closingCredit
) {}
