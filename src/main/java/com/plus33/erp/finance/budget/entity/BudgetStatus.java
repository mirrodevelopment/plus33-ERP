/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : BudgetStatus.java
 * Purpose           : Enumeration of typed constants for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetStatusController
 * Related Service   : BudgetStatusService, BudgetStatusServiceImpl
 * Related Repository: BudgetStatusRepository
 * Related Entity    : BudgetStatus
 * Related DTO       : N/A
 * Related Mapper    : BudgetStatusMapper
 * Related DB Table  : budget_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Finance Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.entity;

public enum BudgetStatus {
    DRAFT,
    SUBMITTED,
    REVIEWED,
    APPROVED,
    LOCKED,
    PARTIALLY_LOCKED,
    ARCHIVED
}
