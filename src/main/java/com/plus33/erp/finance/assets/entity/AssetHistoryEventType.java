/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : AssetHistoryEventType.java
 * Purpose           : Enumeration of typed constants for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetHistoryEventTypeController
 * Related Service   : AssetHistoryEventTypeService, AssetHistoryEventTypeServiceImpl
 * Related Repository: AssetHistoryEventTypeRepository
 * Related Entity    : AssetHistoryEventType
 * Related DTO       : N/A
 * Related Mapper    : AssetHistoryEventTypeMapper
 * Related DB Table  : asset_history_event_types
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Finance Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

public enum AssetHistoryEventType {
    ACQUISITION,
    ASSIGNMENT,
    TRANSFER,
    MAINTENANCE,
    CAPITALIZATION,
    DEPRECIATION,
    REVALUATION,
    IMPAIRMENT,
    DISPOSAL,
    SPLIT,
    MERGE,
    RETIREMENT,
    CHECKOUT,
    RETURN,
    CLAIM,
    ATTACHMENT,
    DOWNTIME,
    WORK_ORDER
}
