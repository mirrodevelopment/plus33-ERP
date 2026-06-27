package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;

public record BudgetDriverRequest(
    Long fiscalYearId,
    String code,
    String name,
    BigDecimal driverValue,
    String unit
) {}
