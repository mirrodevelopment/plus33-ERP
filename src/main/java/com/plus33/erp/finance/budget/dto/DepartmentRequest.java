package com.plus33.erp.finance.budget.dto;

public record DepartmentRequest(
    String code,
    String name,
    Boolean active
) {}
