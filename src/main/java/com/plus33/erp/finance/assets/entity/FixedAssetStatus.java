/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : FixedAssetStatus.java
 * Purpose           : Enumeration of typed constants for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetStatusController
 * Related Service   : FixedAssetStatusService, FixedAssetStatusServiceImpl
 * Related Repository: FixedAssetStatusRepository
 * Related Entity    : FixedAssetStatus
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetStatusMapper
 * Related DB Table  : fixed_asset_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Finance Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

public enum FixedAssetStatus {
    DRAFT,
    UNDER_CONSTRUCTION,
    ACTIVE,
    UNDER_MAINTENANCE,
    TRANSFERRED,
    RETIRED,
    DISPOSAL_REQUESTED,
    DISPOSED,
    WRITTEN_OFF,
    LOST,
    STOLEN,
    SPLIT,
    MERGED,
    EXPENSED
}
