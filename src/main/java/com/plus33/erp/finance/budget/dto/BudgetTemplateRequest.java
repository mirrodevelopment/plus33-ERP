/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : BudgetTemplateRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetTemplateController
 * Related Service   : BudgetTemplateService, BudgetTemplateServiceImpl
 * Related Repository: BudgetTemplateRepository
 * Related Entity    : BudgetTemplate
 * Related DTO       : BudgetTemplateLineRequest, BudgetTemplateRequest
 * Related Mapper    : BudgetTemplateMapper
 * Related DB Table  : budget_templates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetTemplateController, BudgetTemplateService, BudgetTemplateServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.dto;

import java.util.List;

public record BudgetTemplateRequest(
    String code,
    String name,
    String description,
    String industryType,
    Boolean active,
    List<BudgetTemplateLineRequest> lines
) {}
