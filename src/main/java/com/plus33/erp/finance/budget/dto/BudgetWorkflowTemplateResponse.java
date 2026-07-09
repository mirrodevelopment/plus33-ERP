/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : BudgetWorkflowTemplateResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetWorkflowTemplateController
 * Related Service   : BudgetWorkflowTemplateService, BudgetWorkflowTemplateServiceImpl
 * Related Repository: BudgetWorkflowTemplateRepository
 * Related Entity    : BudgetWorkflowTemplate
 * Related DTO       : BudgetWorkflowStepResponse, BudgetWorkflowTemplateResponse
 * Related Mapper    : BudgetWorkflowTemplateMapper
 * Related DB Table  : budget_workflow_templates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetWorkflowTemplateController, BudgetWorkflowTemplateService, BudgetWorkflowTemplateServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.dto;

import java.util.List;

public record BudgetWorkflowTemplateResponse(
    Long id,
    Long companyId,
    String code,
    String name,
    Boolean active,
    List<BudgetWorkflowStepResponse> steps
) {}
