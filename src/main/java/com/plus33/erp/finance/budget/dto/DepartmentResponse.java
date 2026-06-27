package com.plus33.erp.finance.budget.dto;

public record DepartmentResponse(
    Long id,
    Long companyId,
    String code,
    String name,
    Boolean active
) {}
