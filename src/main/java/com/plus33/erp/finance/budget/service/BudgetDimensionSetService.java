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
