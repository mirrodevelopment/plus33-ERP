/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : BudgetLineRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetLineController
 * Related Service   : BudgetLineService, BudgetLineServiceImpl
 * Related Repository: BudgetLineRepository
 * Related Entity    : BudgetLine
 * Related DTO       : BudgetDimensionSetRequest, BudgetLineRequest
 * Related Mapper    : BudgetLineMapper
 * Related DB Table  : budget_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetLineController, BudgetLineService, BudgetLineServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetLineRequest(
    Long accountId,
    BudgetDimensionSetRequest dimensionSet,
    LocalDate periodStartDate,
    LocalDate periodEndDate,
    BigDecimal allocatedAmount,
    String distributionMethod, // EQUAL, MANUAL, SEASONAL, PREV_YEAR_ACTUAL, PERCENTAGE, FORMULA
    String formulaExpression,
    String notes
) {}
