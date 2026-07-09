/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : BudgetRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetController
 * Related Service   : BudgetService, BudgetServiceImpl
 * Related Repository: BudgetRepository
 * Related Entity    : Budget
 * Related DTO       : BudgetLineRequest, BudgetRequest
 * Related Mapper    : BudgetMapper
 * Related DB Table  : budgets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetController, BudgetService, BudgetServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.util.List;

public record BudgetRequest(
    Long fiscalYearId,
    Long budgetPolicyId,
    Long workflowTemplateId,
    String code,
    String name,
    String budgetType, // OPERATING, CAPITAL
    String periodType, // ANNUAL, QUARTERLY, MONTHLY, WEEKLY, CUSTOM
    String scenario, // EXPECTED, BEST_CASE, WORST_CASE
    Boolean isForecast,
    String forecastType,
    String forecastCycleCode,
    String rateLockType, // HISTORICAL, BUDGET_RATE, MONTHLY_AVERAGE, SPOT
    BigDecimal budgetExchangeRate,
    List<BudgetLineRequest> lines
) {}
