package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BudgetDriverResponse(
    Long id,
    Long companyId,
    Long fiscalYearId,
    String code,
    String name,
    BigDecimal driverValue,
    String unit,
    LocalDateTime createdAt
) {}
