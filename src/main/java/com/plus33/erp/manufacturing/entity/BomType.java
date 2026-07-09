/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : BomType.java
 * Purpose           : Enumeration of typed constants for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BomTypeController
 * Related Service   : BomTypeService, BomTypeServiceImpl
 * Related Repository: BomTypeRepository
 * Related Entity    : BomType
 * Related DTO       : N/A
 * Related Mapper    : BomTypeMapper
 * Related DB Table  : bom_types
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Manufacturing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Manufacturing Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

public enum BomType {
    MANUFACTURING,
    ENGINEERING,
    PHANTOM,
    SALES
}
