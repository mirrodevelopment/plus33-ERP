/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.entity
 * File              : TaxProfile.java
 * Purpose           : Enumeration of typed constants for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxProfileController
 * Related Service   : TaxProfileService, TaxProfileServiceImpl
 * Related Repository: TaxProfileRepository
 * Related Entity    : TaxProfile
 * Related DTO       : N/A
 * Related Mapper    : TaxProfileMapper
 * Related DB Table  : tax_profiles
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Sales Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.sales.entity;

public enum TaxProfile {
    STANDARD,
    EXEMPT,
    EXPORT,
    REVERSE_CHARGE
}
