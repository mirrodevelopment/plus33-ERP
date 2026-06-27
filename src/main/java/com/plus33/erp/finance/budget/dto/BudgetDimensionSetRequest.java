package com.plus33.erp.finance.budget.dto;

public record BudgetDimensionSetRequest(
    Long departmentId,
    Long costCenterId,
    Long projectId,
    Long warehouseId,
    Long assetCategoryId,
    Long regionId,
    Long storeId
) {}
