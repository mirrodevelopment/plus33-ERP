/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetAuditItemRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetAuditItemController
 * Related Service   : AssetAuditItemService, AssetAuditItemServiceImpl
 * Related Repository: AssetAuditItemRepository
 * Related Entity    : AssetAuditItem
 * Related DTO       : AssetAuditItemRequest
 * Related Mapper    : AssetAuditItemMapper
 * Related DB Table  : asset_audit_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetAuditItemController, AssetAuditItemService, AssetAuditItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

public record AssetAuditItemRequest(
    Long fixedAssetId,
    String result, // FOUND_OK, DAMAGED, MISSING
    String remarks,
    String photoEvidenceUrl
) {}
