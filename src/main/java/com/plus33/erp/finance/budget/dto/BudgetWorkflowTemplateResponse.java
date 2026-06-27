package com.plus33.erp.finance.budget.dto;

import java.util.List;

public record BudgetWorkflowTemplateResponse(
    Long id,
    Long companyId,
    String code,
    String name,
    Boolean active,
    List<BudgetWorkflowStepResponse> steps
) {}
