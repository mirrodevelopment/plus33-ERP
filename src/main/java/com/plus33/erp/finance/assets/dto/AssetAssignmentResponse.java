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
