package com.plus33.erp.finance.budget.dto;

import java.util.List;

public record BudgetComparisonResponse(
    Long budgetId1,
    String budgetCode1,
    Long budgetId2,
    String budgetCode2,
    List<BudgetComparisonItem> items
) {}
