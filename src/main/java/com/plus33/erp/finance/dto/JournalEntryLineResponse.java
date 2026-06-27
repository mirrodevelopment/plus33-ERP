package com.plus33.erp.finance.dto;

import java.math.BigDecimal;

public record JournalEntryLineResponse(
        Long id,
        Long accountId,
        String accountCode,
        String accountName,
        BigDecimal debitAmount,
        BigDecimal creditAmount,
        Long dimensionSetId
) {}
