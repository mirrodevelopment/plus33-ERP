/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : BudgetDrilldownResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetDrilldownController
 * Related Service   : BudgetDrilldownService, BudgetDrilldownServiceImpl
 * Related Repository: BudgetDrilldownRepository
 * Related Entity    : BudgetDrilldown
 * Related DTO       : BudgetDrilldownResponse
 * Related Mapper    : BudgetDrilldownMapper
 * Related DB Table  : budget_drilldowns
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetDrilldownController, BudgetDrilldownService, BudgetDrilldownServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.util.List;

public record BudgetDrilldownResponse(
    String dimensionName, // e.g. "Department", "Cost Center", "Project", "Account"
    String dimensionValue,
    BigDecimal totalAllocated,
    BigDecimal totalReserved,
    BigDecimal totalConsumed,
    BigDecimal totalAvailable,
    BigDecimal utilizationPercent,
    List<BudgetDrilldownResponse> children
) {}
