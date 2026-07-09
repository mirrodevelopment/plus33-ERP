/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : BudgetWorkflowStepRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetWorkflowStepController
 * Related Service   : BudgetWorkflowStepService, BudgetWorkflowStepServiceImpl
 * Related Repository: BudgetWorkflowStepRepository
 * Related Entity    : BudgetWorkflowStep
 * Related DTO       : BudgetWorkflowStepRequest
 * Related Mapper    : BudgetWorkflowStepMapper
 * Related DB Table  : budget_workflow_steps
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetWorkflowStepController, BudgetWorkflowStepService, BudgetWorkflowStepServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;

public record BudgetWorkflowStepRequest(
    Integer stepSequence,
    String roleCode,
    BigDecimal minAmount,
    Boolean active
) {}
