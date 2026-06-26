package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;

public record ProjectedJournalEntryLine(
    Long accountId,
    String accountCode,
    String accountName,
    BigDecimal debitAmount,
    BigDecimal creditAmount
) {}
