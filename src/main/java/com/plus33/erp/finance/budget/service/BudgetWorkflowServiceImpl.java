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

@Service
@RequiredArgsConstructor
public class BudgetWorkflowServiceImpl implements BudgetWorkflowService {

    private final BudgetWorkflowTemplateRepository workflowTemplateRepository;
    private final CompanyRepository companyRepository;

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

    @Override
    @Transactional(readOnly = true)
    public BudgetWorkflowTemplateResponse getTemplate(Long id) {
        BudgetWorkflowTemplate template = workflowTemplateRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Workflow Template not found"));
        return mapToResponse(template);
    }

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
