package com.plus33.erp.finance.budget.service;

import com.plus33.erp.finance.budget.dto.BudgetWorkflowTemplateRequest;
import com.plus33.erp.finance.budget.dto.BudgetWorkflowTemplateResponse;

import java.util.List;

public interface BudgetWorkflowService {
    BudgetWorkflowTemplateResponse createTemplate(Long companyId, BudgetWorkflowTemplateRequest request);
    BudgetWorkflowTemplateResponse updateTemplate(Long id, BudgetWorkflowTemplateRequest request);
    BudgetWorkflowTemplateResponse getTemplate(Long id);
    List<BudgetWorkflowTemplateResponse> getTemplatesByCompany(Long companyId);
}
