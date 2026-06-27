package com.plus33.erp.finance.budget.dto;

public record CostCenterResponse(
    Long id,
    Long companyId,
    String code,
    String name,
    Boolean active
) {}
