/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : BomStatus.java
 * Purpose           : Enumeration of typed constants for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BomStatusController
 * Related Service   : BomStatusService, BomStatusServiceImpl
 * Related Repository: BomStatusRepository
 * Related Entity    : BomStatus
 * Related DTO       : N/A
 * Related Mapper    : BomStatusMapper
 * Related DB Table  : bom_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Manufacturing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Manufacturing Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

public enum BomStatus {
    DRAFT,
    PENDING_APPROVAL,
    APPROVED,
    ACTIVE,
    SUPERSEDED,
    OBSOLETE
}
