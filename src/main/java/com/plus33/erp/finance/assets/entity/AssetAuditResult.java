/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : AssetAuditResult.java
 * Purpose           : Enumeration of typed constants for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetAuditResultController
 * Related Service   : AssetAuditResultService, AssetAuditResultServiceImpl
 * Related Repository: AssetAuditResultRepository
 * Related Entity    : AssetAuditResult
 * Related DTO       : N/A
 * Related Mapper    : AssetAuditResultMapper
 * Related DB Table  : asset_audit_results
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Finance Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

public enum AssetAuditResult {
    FOUND_OK,
    DAMAGED,
    MISSING
}
