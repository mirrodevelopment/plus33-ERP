/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : BudgetPeriodType.java
 * Purpose           : Enumeration of typed constants for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetPeriodTypeController
 * Related Service   : BudgetPeriodTypeService, BudgetPeriodTypeServiceImpl
 * Related Repository: BudgetPeriodTypeRepository
 * Related Entity    : BudgetPeriodType
 * Related DTO       : N/A
 * Related Mapper    : BudgetPeriodTypeMapper
 * Related DB Table  : budget_period_types
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Finance Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.entity;

public enum BudgetPeriodType {
    ANNUAL,
    QUARTERLY,
    MONTHLY,
    WEEKLY,
    CUSTOM
}
