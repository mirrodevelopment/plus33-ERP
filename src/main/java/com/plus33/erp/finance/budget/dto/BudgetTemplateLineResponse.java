/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : BudgetTemplateLineResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetTemplateLineController
 * Related Service   : BudgetTemplateLineService, BudgetTemplateLineServiceImpl
 * Related Repository: BudgetTemplateLineRepository
 * Related Entity    : BudgetTemplateLine
 * Related DTO       : BudgetTemplateLineResponse
 * Related Mapper    : BudgetTemplateLineMapper
 * Related DB Table  : budget_template_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetTemplateLineController, BudgetTemplateLineService, BudgetTemplateLineServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;

public record BudgetTemplateLineResponse(
    Long id,
    String accountCode,
    String dimensionType,
    BigDecimal distributionPercent
) {}
