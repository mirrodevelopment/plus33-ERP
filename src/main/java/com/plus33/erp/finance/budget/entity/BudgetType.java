/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : BudgetType.java
 * Purpose           : Enumeration of typed constants for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetTypeController
 * Related Service   : BudgetTypeService, BudgetTypeServiceImpl
 * Related Repository: BudgetTypeRepository
 * Related Entity    : BudgetType
 * Related DTO       : N/A
 * Related Mapper    : BudgetTypeMapper
 * Related DB Table  : budget_types
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Finance Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.entity;

public enum BudgetType {
    OPERATING,
    CAPITAL
}
