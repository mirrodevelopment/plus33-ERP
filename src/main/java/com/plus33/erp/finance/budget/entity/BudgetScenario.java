/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : BudgetScenario.java
 * Purpose           : Enumeration of typed constants for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetScenarioController
 * Related Service   : BudgetScenarioService, BudgetScenarioServiceImpl
 * Related Repository: BudgetScenarioRepository
 * Related Entity    : BudgetScenario
 * Related DTO       : N/A
 * Related Mapper    : BudgetScenarioMapper
 * Related DB Table  : budget_scenarios
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Finance Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.entity;

public enum BudgetScenario {
    EXPECTED,
    BEST_CASE,
    WORST_CASE
}
