/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : LeaseType.java
 * Purpose           : Enumeration of typed constants for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LeaseTypeController
 * Related Service   : LeaseTypeService, LeaseTypeServiceImpl
 * Related Repository: LeaseTypeRepository
 * Related Entity    : LeaseType
 * Related DTO       : N/A
 * Related Mapper    : LeaseTypeMapper
 * Related DB Table  : lease_types
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Finance Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

public enum LeaseType {
    OPERATING,
    FINANCE
}
