/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : BudgetMassUpdateRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetMassUpdateController
 * Related Service   : BudgetMassUpdateService, BudgetMassUpdateServiceImpl
 * Related Repository: BudgetMassUpdateRepository
 * Related Entity    : BudgetMassUpdate
 * Related DTO       : BudgetMassUpdateRequest
 * Related Mapper    : BudgetMassUpdateMapper
 * Related DB Table  : budget_mass_updates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetMassUpdateController, BudgetMassUpdateService, BudgetMassUpdateServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.util.List;

public record BudgetMassUpdateRequest(
    List<Long> budgetLineIds,
    String adjustmentType, // PERCENTAGE, FIXED_AMOUNT
    BigDecimal adjustmentValue, // e.g. 10.00 for +10% or -5.00 for -5% or fixed amount delta
    String reason
) {}
