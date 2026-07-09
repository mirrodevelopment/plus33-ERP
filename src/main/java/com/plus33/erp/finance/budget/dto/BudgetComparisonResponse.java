/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : BudgetComparisonResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetComparisonController
 * Related Service   : BudgetComparisonService, BudgetComparisonServiceImpl
 * Related Repository: BudgetComparisonRepository
 * Related Entity    : BudgetComparison
 * Related DTO       : BudgetComparisonResponse
 * Related Mapper    : BudgetComparisonMapper
 * Related DB Table  : budget_comparisons
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetComparisonController, BudgetComparisonService, BudgetComparisonServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.dto;

import java.util.List;

public record BudgetComparisonResponse(
    Long budgetId1,
    String budgetCode1,
    Long budgetId2,
    String budgetCode2,
    List<BudgetComparisonItem> items
) {}
