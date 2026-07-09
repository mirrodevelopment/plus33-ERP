/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : FixedAssetAuditStatus.java
 * Purpose           : Enumeration of typed constants for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetAuditStatusController
 * Related Service   : FixedAssetAuditStatusService, FixedAssetAuditStatusServiceImpl
 * Related Repository: FixedAssetAuditStatusRepository
 * Related Entity    : FixedAssetAuditStatus
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetAuditStatusMapper
 * Related DB Table  : fixed_asset_audit_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Finance Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

public enum FixedAssetAuditStatus {
    SCHEDULED,
    IN_PROGRESS,
    VERIFIED,
    DISCREPANCY_REPORT,
    ADJUSTMENT_APPROVED
}
