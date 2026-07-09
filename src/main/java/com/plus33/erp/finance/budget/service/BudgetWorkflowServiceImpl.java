/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.service
 * File              : BudgetWorkflowServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetWorkflowController
 * Related Service   : BudgetWorkflowServiceImpl
 * Related Repository: BudgetWorkflowTemplateRepository, CompanyRepository
 * Related Entity    : BudgetWorkflow
 * Related DTO       : BudgetWorkflowStepResponse, BudgetWorkflowTemplateRequest, BudgetWorkflowTemplateResponse, mapToResponse
 * Related Mapper    : BudgetWorkflowMapper
 * Related DB Table  : budget_workflows
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module
 * Used By           : BudgetWorkflowController, BudgetWorkflowServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements BudgetWorkflowService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.budget.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.finance.budget.dto.BudgetWorkflowStepResponse;
import com.plus33.erp.finance.budget.dto.BudgetWorkflowTemplateRequest;
import com.plus33.erp.finance.budget.dto.BudgetWorkflowTemplateResponse;
import com.plus33.erp.finance.budget.entity.BudgetWorkflowStep;
import com.plus33.erp.finance.budget.entity.BudgetWorkflowTemplate;
import com.plus33.erp.finance.budget.repository.BudgetWorkflowTemplateRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetWorkflowServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * BudgetWorkflowController
 *   --> BudgetWorkflowServiceImpl (this)
 *   --> Validate business rules
 *   --> BudgetWorkflowRepository (read/write 'budget_workflows')
 *   --> BudgetWorkflowMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code budget_workflows}</p>
 * <p><b>Module Deps      :</b> Common, Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class BudgetWorkflowServiceImpl implements BudgetWorkflowService {

    private final BudgetWorkflowTemplateRepository workflowTemplateRepository;
    private final CompanyRepository companyRepository;

    /**
     * Creates a new template and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param request the validated request DTO containing input data
     * @return the BudgetWorkflowTemplateResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public BudgetWorkflowTemplateResponse createTemplate(Long companyId, BudgetWorkflowTemplateRequest request) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new BusinessException("Company not found"));

        if (workflowTemplateRepository.findByCompanyIdAndCode(companyId, request.code()).isPresent()) {
            throw new BusinessException("Workflow Template code already exists: " + request.code());
        }

        BudgetWorkflowTemplate template = BudgetWorkflowTemplate.builder()
            .company(company)
            .code(request.code())
            .name(request.name())
            .active(request.active() != null ? request.active() : true)
            .build();

        if (request.steps() != null) {
            List<BudgetWorkflowStep> steps = request.steps().stream()
                .map(stepReq -> BudgetWorkflowStep.builder()
                    .template(template)
                    .stepSequence(stepReq.stepSequence())
                    .roleCode(stepReq.roleCode())
                    .minAmount(stepReq.minAmount())
                    .active(stepReq.active() != null ? stepReq.active() : true)
                    .build())
                .toList();
            template.getSteps().addAll(steps);
        }

        return mapToResponse(workflowTemplateRepository.save(template));
    }

    /**
     * Updates an existing template record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the BudgetWorkflowTemplateResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public BudgetWorkflowTemplateResponse updateTemplate(Long id, BudgetWorkflowTemplateRequest request) {
        BudgetWorkflowTemplate template = workflowTemplateRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Workflow Template not found"));

        template.setName(request.name());
        if (request.active() != null) {
            template.setActive(request.active());
        }

        template.getSteps().clear();
        if (request.steps() != null) {
            List<BudgetWorkflowStep> steps = request.steps().stream()
                .map(stepReq -> BudgetWorkflowStep.builder()
                    .template(template)
                    .stepSequence(stepReq.stepSequence())
                    .roleCode(stepReq.roleCode())
                    .minAmount(stepReq.minAmount())
                    .active(stepReq.active() != null ? stepReq.active() : true)
                    .build())
                .toList();
            template.getSteps().addAll(steps);
        }

        return mapToResponse(workflowTemplateRepository.save(template));
    }

    /**
     * Retrieves template data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the BudgetWorkflowTemplateResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public BudgetWorkflowTemplateResponse getTemplate(Long id) {
        BudgetWorkflowTemplate template = workflowTemplateRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Workflow Template not found"));
        return mapToResponse(template);
    }

    /**
     * Retrieves templates by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<BudgetWorkflowTemplateResponse> getTemplatesByCompany(Long companyId) {
        return workflowTemplateRepository.findAll().stream()
            .filter(t -> t.getCompany().getId().equals(companyId))
            .map(this::mapToResponse)
            .toList();
    }

    private BudgetWorkflowTemplateResponse mapToResponse(BudgetWorkflowTemplate template) {
        List<BudgetWorkflowStepResponse> steps = template.getSteps().stream()
            .map(step -> new BudgetWorkflowStepResponse(
                step.getId(),
                step.getStepSequence(),
                step.getRoleCode(),
                step.getMinAmount(),
                step.getActive()
            ))
            .toList();

        return new BudgetWorkflowTemplateResponse(
            template.getId(),
            template.getCompany().getId(),
            template.getCode(),
            template.getName(),
            template.getActive(),
            steps
        );
    }
}