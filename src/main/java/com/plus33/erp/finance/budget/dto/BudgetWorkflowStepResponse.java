package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;

public record BudgetWorkflowStepResponse(
    Long id,
    Integer stepSequence,
    String roleCode,
    BigDecimal minAmount,
    Boolean active
) {}
