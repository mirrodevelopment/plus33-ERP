package com.plus33.erp.finance.budget.dto;

public record CostCenterRequest(
    String code,
    String name,
    Boolean active
) {}
