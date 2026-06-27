package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.util.List;

public record BudgetDrilldownResponse(
    String dimensionName, // e.g. "Department", "Cost Center", "Project", "Account"
    String dimensionValue,
    BigDecimal totalAllocated,
    BigDecimal totalReserved,
    BigDecimal totalConsumed,
    BigDecimal totalAvailable,
    BigDecimal utilizationPercent,
    List<BudgetDrilldownResponse> children
) {}
