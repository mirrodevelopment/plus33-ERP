package com.plus33.erp.finance.reporting.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record IncomeStatementResponse(
    List<IncomeStatementEntry> revenues,
    List<IncomeStatementEntry> expenses,
    BigDecimal totalRevenue,
    BigDecimal totalExpenses,
    BigDecimal netIncome,
    LocalDate startDate,
    LocalDate endDate,
    LocalDateTime generatedAt,
    String generatedBy
) {}
