package com.plus33.erp.finance.budget.service;

import com.plus33.erp.finance.budget.dto.CostCenterRequest;
import com.plus33.erp.finance.budget.dto.CostCenterResponse;

import java.util.List;

public interface CostCenterService {
    CostCenterResponse createCostCenter(Long companyId, CostCenterRequest request);
    CostCenterResponse updateCostCenter(Long id, CostCenterRequest request);
    CostCenterResponse getCostCenter(Long id);
    List<CostCenterResponse> getCostCentersByCompany(Long companyId);
}
