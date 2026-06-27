package com.plus33.erp.finance.budget.dto;

public record BudgetDimensionSetResponse(
    Long id,
    Long companyId,
    Long departmentId,
    String departmentCode,
    String departmentName,
    Long costCenterId,
    String costCenterCode,
    String costCenterName,
    Long projectId,
    String projectCode,
    String projectName,
    Long warehouseId,
    String warehouseCode,
    String warehouseName,
    Long assetCategoryId,
    String assetCategoryCode,
    String assetCategoryName,
    Long regionId,
    String regionCode,
    String regionName,
    Long storeId,
    String storeCode,
    String storeName
) {}
