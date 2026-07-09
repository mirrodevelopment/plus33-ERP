/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : BudgetRevisionRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetRevisionController
 * Related Service   : BudgetRevisionService, BudgetRevisionServiceImpl
 * Related Repository: BudgetRevisionRepository
 * Related Entity    : BudgetRevision
 * Related DTO       : BudgetRevisionRequest
 * Related Mapper    : BudgetRevisionMapper
 * Related DB Table  : budget_revisions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetRevisionController, BudgetRevisionService, BudgetRevisionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetRevisionRequest(
    Long budgetLineId,
    LocalDate revisionDate,
    BigDecimal newAmount,
    String reason
) {}
