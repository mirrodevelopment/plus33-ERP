package com.plus33.erp.finance.budget.dto;

import java.util.List;

public record BudgetWorkflowTemplateRequest(
    String code,
    String name,
    Boolean active,
    List<BudgetWorkflowStepRequest> steps
) {}
