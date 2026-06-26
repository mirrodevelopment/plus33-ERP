package com.plus33.erp.finance.reporting.dto;

import com.plus33.erp.finance.reporting.entity.ExchangeRateType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record LedgerBalanceSnapshot(
    Map<Long, BigDecimal> debitBalances,
    Map<Long, BigDecimal> creditBalances,
    LocalDate startDate,
    LocalDate endDate,
    String currency,
    ExchangeRateType rateType
) {}
