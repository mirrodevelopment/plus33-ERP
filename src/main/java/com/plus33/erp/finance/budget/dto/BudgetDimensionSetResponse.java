/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : BudgetDimensionSetResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetDimensionSetController
 * Related Service   : BudgetDimensionSetService, BudgetDimensionSetServiceImpl
 * Related Repository: BudgetDimensionSetRepository
 * Related Entity    : BudgetDimensionSet
 * Related DTO       : BudgetDimensionSetResponse
 * Related Mapper    : BudgetDimensionSetMapper
 * Related DB Table  : budget_dimension_sets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetDimensionSetController, BudgetDimensionSetService, BudgetDimensionSetServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
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
