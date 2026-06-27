package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;

public record BudgetWorkflowStepRequest(
    Integer stepSequence,
    String roleCode,
    BigDecimal minAmount,
    Boolean active
) {}
