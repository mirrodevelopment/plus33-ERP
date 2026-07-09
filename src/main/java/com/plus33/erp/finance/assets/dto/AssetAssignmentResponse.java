/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetAssignmentResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetAssignmentController
 * Related Service   : AssetAssignmentService, AssetAssignmentServiceImpl
 * Related Repository: AssetAssignmentRepository
 * Related Entity    : AssetAssignment
 * Related DTO       : AssetAssignmentResponse
 * Related Mapper    : AssetAssignmentMapper
 * Related DB Table  : asset_assignments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetAssignmentController, AssetAssignmentService, AssetAssignmentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.time.LocalDateTime;

public record AssetAssignmentResponse(
    Long id,
    Long fixedAssetId,
    Long assignedEmployeeId,
    String assignedEmployeeName,
    String assignedDepartment,
    Long assignedWarehouseId,
    String assignedWarehouseName,
    Long assignedStoreId,
    String assignedStoreName,
    LocalDateTime assignedAt,
    LocalDateTime releasedAt,
    String assignedBy
) {}
