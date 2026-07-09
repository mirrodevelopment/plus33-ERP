/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : BudgetDriverRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetDriverController
 * Related Service   : BudgetDriverService, BudgetDriverServiceImpl
 * Related Repository: BudgetDriverRepository
 * Related Entity    : BudgetDriver
 * Related DTO       : BudgetDriverRequest
 * Related Mapper    : BudgetDriverMapper
 * Related DB Table  : budget_drivers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetDriverController, BudgetDriverService, BudgetDriverServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;

public record BudgetDriverRequest(
    Long fiscalYearId,
    String code,
    String name,
    BigDecimal driverValue,
    String unit
) {}
