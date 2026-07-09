/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : BudgetPolicyRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetPolicyController
 * Related Service   : BudgetPolicyService, BudgetPolicyServiceImpl
 * Related Repository: BudgetPolicyRepository
 * Related Entity    : BudgetPolicy
 * Related DTO       : BudgetPolicyRequest
 * Related Mapper    : BudgetPolicyMapper
 * Related DB Table  : budget_policys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetPolicyController, BudgetPolicyService, BudgetPolicyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
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
