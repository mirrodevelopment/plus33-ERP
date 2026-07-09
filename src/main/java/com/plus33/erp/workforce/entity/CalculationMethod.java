/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : CalculationMethod.java
 * Purpose           : Enumeration of typed constants for Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CalculationMethodController
 * Related Service   : CalculationMethodService, CalculationMethodServiceImpl
 * Related Repository: CalculationMethodRepository
 * Related Entity    : CalculationMethod
 * Related DTO       : N/A
 * Related Mapper    : CalculationMethodMapper
 * Related DB Table  : calculation_methods
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Workforce Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

public enum CalculationMethod {
    FIXED,
    PERCENTAGE,
    FORMULA,
    PROGRESSIVE_SLAB
}
