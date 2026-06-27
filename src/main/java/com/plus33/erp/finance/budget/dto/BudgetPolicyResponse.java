package com.plus33.erp.finance.budget.dto;

public record BudgetPolicyResponse(
    Long id,
    Long companyId,
    String code,
    String name,
    String controlType,
    Boolean allowNegative,
    Boolean allowTransfers,
    Boolean allowRevisions,
    Boolean autoReserve,
    Boolean autoConsume,
    Boolean approvalRequired,
    Boolean multiCurrencyEnabled,
    Boolean active
) {}
