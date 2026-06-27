package com.plus33.erp.finance.budget.service;

import com.plus33.erp.finance.budget.dto.BudgetPolicyRequest;
import com.plus33.erp.finance.budget.dto.BudgetPolicyResponse;

import java.util.List;

public interface BudgetPolicyService {
    BudgetPolicyResponse createPolicy(Long companyId, BudgetPolicyRequest request);
    BudgetPolicyResponse updatePolicy(Long id, BudgetPolicyRequest request);
    BudgetPolicyResponse getPolicy(Long id);
    List<BudgetPolicyResponse> getPoliciesByCompany(Long companyId);
}
