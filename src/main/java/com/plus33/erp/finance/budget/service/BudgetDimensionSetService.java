/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.service
 * File              : BudgetDimensionSetService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetDimensionSetController
 * Related Service   : BudgetDimensionSetService, BudgetDimensionSetServiceImpl
 * Related Repository: BudgetDimensionSetRepository
 * Related Entity    : BudgetDimensionSet
 * Related DTO       : BudgetDimensionSetRequest
 * Related Mapper    : BudgetDimensionSetMapper
 * Related DB Table  : budget_dimension_sets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.budget.service;

import com.plus33.erp.finance.budget.dto.BudgetDimensionSetRequest;
import com.plus33.erp.finance.budget.entity.BudgetDimensionSet;

public interface BudgetDimensionSetService {
    BudgetDimensionSet getOrCreateDimensionSet(Long companyId, BudgetDimensionSetRequest request);
    BudgetDimensionSet getOrCreateDimensionSet(
        Long companyId,
        Long departmentId,
        Long costCenterId,
        Long projectId,
        Long warehouseId,
        Long assetCategoryId,
        Long regionId,
        Long storeId
    );
}
