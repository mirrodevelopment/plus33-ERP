package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;

public record BudgetTemplateLineResponse(
    Long id,
    String accountCode,
    String dimensionType,
    BigDecimal distributionPercent
) {}
