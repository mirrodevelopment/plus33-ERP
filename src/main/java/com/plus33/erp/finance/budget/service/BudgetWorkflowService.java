/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.service
 * File              : BudgetWorkflowService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetWorkflowController
 * Related Service   : BudgetWorkflowService, BudgetWorkflowServiceImpl
 * Related Repository: BudgetWorkflowRepository
 * Related Entity    : BudgetWorkflow
 * Related DTO       : BudgetWorkflowTemplateRequest, BudgetWorkflowTemplateResponse
 * Related Mapper    : BudgetWorkflowMapper
 * Related DB Table  : budget_workflows
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.budget.service;

import com.plus33.erp.finance.budget.dto.BudgetWorkflowTemplateRequest;
import com.plus33.erp.finance.budget.dto.BudgetWorkflowTemplateResponse;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetWorkflowService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface BudgetWorkflowService {
    BudgetWorkflowTemplateResponse createTemplate(Long companyId, BudgetWorkflowTemplateRequest request);
    BudgetWorkflowTemplateResponse updateTemplate(Long id, BudgetWorkflowTemplateRequest request);
    BudgetWorkflowTemplateResponse getTemplate(Long id);
    List<BudgetWorkflowTemplateResponse> getTemplatesByCompany(Long companyId);
}
