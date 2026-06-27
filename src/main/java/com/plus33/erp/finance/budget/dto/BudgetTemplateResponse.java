package com.plus33.erp.finance.budget.dto;

import java.util.List;

public record BudgetTemplateResponse(
    Long id,
    String code,
    String name,
    String description,
    String industryType,
    Boolean active,
    List<BudgetTemplateLineResponse> lines
) {}
