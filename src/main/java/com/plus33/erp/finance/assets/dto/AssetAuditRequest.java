/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetAuditRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetAuditController
 * Related Service   : AssetAuditService, AssetAuditServiceImpl
 * Related Repository: AssetAuditRepository
 * Related Entity    : AssetAudit
 * Related DTO       : AssetAuditItemRequest, AssetAuditRequest
 * Related Mapper    : AssetAuditMapper
 * Related DB Table  : asset_audits
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetAuditController, AssetAuditService, AssetAuditServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.time.LocalDate;
import java.util.List;

public record AssetAuditRequest(
    LocalDate auditDate,
    String auditorName,
    Long warehouseId,
    Long storeId,
    List<AssetAuditItemRequest> items
) {}
