package com.plus33.erp.finance.budget.dto;

public record BudgetPolicyRequest(
    String code,
    String name,
    String controlType, // HARD, SOFT, NONE
    Boolean allowNegative,
    Boolean allowTransfers,
    Boolean allowRevisions,
    Boolean autoReserve,
    Boolean autoConsume,
    Boolean approvalRequired,
    Boolean multiCurrencyEnabled,
    Boolean active
) {}
