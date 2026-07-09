/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.entity
 * File              : PickListStatus.java
 * Purpose           : Enumeration of typed constants for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PickListStatusController
 * Related Service   : PickListStatusService, PickListStatusServiceImpl
 * Related Repository: PickListStatusRepository
 * Related Entity    : PickListStatus
 * Related DTO       : N/A
 * Related Mapper    : PickListStatusMapper
 * Related DB Table  : pick_list_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Sales Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.sales.entity;

public enum PickListStatus {
    DRAFT,
    RELEASED,
    PICKING,
    PICKED,
    PACKED,
    SHIPPED,
    CANCELLED
}
