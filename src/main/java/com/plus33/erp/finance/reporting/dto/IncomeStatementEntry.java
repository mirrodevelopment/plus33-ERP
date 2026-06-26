package com.plus33.erp.finance.reporting.dto;

import java.math.BigDecimal;

public record IncomeStatementEntry(
    Long accountId,
    String accountCode,
    String accountName,
    BigDecimal balance
) {}
