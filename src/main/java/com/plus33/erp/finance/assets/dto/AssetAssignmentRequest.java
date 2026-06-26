package com.plus33.erp.finance.assets.dto;

public record AssetAssignmentRequest(
    Long assignedEmployeeId,
    String assignedDepartment,
    Long assignedWarehouseId,
    Long assignedStoreId
) {}
