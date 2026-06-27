package com.plus33.erp.finance.budget.service;

import com.plus33.erp.finance.budget.dto.BudgetTemplateRequest;
import com.plus33.erp.finance.budget.dto.BudgetTemplateResponse;

import java.util.List;

public interface BudgetTemplateService {
    BudgetTemplateResponse createTemplate(BudgetTemplateRequest request);
    BudgetTemplateResponse updateTemplate(Long id, BudgetTemplateRequest request);
    BudgetTemplateResponse getTemplate(Long id);
    List<BudgetTemplateResponse> getAllTemplates();
}
