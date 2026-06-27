package com.plus33.erp.finance.budget.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.finance.budget.dto.BudgetTemplateLineResponse;
import com.plus33.erp.finance.budget.dto.BudgetTemplateRequest;
import com.plus33.erp.finance.budget.dto.BudgetTemplateResponse;
import com.plus33.erp.finance.budget.entity.BudgetTemplate;
import com.plus33.erp.finance.budget.entity.BudgetTemplateLine;
import com.plus33.erp.finance.budget.repository.BudgetTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetTemplateServiceImpl implements BudgetTemplateService {

    private final BudgetTemplateRepository budgetTemplateRepository;

    @Override
    @Transactional
    public BudgetTemplateResponse createTemplate(BudgetTemplateRequest request) {
        if (budgetTemplateRepository.findByCode(request.code()).isPresent()) {
            throw new BusinessException("Budget Template code already exists: " + request.code());
        }

        BudgetTemplate template = BudgetTemplate.builder()
            .code(request.code())
            .name(request.name())
            .description(request.description())
            .industryType(request.industryType())
            .active(request.active() != null ? request.active() : true)
            .build();

        if (request.lines() != null) {
            List<BudgetTemplateLine> lines = request.lines().stream()
                .map(lineReq -> BudgetTemplateLine.builder()
                    .template(template)
                    .accountCode(lineReq.accountCode())
                    .dimensionType(lineReq.dimensionType())
                    .distributionPercent(lineReq.distributionPercent())
                    .build())
                .toList();
            template.getLines().addAll(lines);
        }

        return mapToResponse(budgetTemplateRepository.save(template));
    }

    @Override
    @Transactional
    public BudgetTemplateResponse updateTemplate(Long id, BudgetTemplateRequest request) {
        BudgetTemplate template = budgetTemplateRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Budget Template not found"));

        template.setName(request.name());
        template.setDescription(request.description());
        template.setIndustryType(request.industryType());
        if (request.active() != null) {
            template.setActive(request.active());
        }

        template.getLines().clear();
        if (request.lines() != null) {
            List<BudgetTemplateLine> lines = request.lines().stream()
                .map(lineReq -> BudgetTemplateLine.builder()
                    .template(template)
                    .accountCode(lineReq.accountCode())
                    .dimensionType(lineReq.dimensionType())
                    .distributionPercent(lineReq.distributionPercent())
                    .build())
                .toList();
            template.getLines().addAll(lines);
        }

        return mapToResponse(budgetTemplateRepository.save(template));
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetTemplateResponse getTemplate(Long id) {
        BudgetTemplate template = budgetTemplateRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Budget Template not found"));
        return mapToResponse(template);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BudgetTemplateResponse> getAllTemplates() {
        return budgetTemplateRepository.findAll().stream()
            .map(this::mapToResponse)
            .toList();
    }

    private BudgetTemplateResponse mapToResponse(BudgetTemplate template) {
        List<BudgetTemplateLineResponse> lines = template.getLines().stream()
            .map(line -> new BudgetTemplateLineResponse(
                line.getId(),
                line.getAccountCode(),
                line.getDimensionType(),
                line.getDistributionPercent()
            ))
            .toList();

        return new BudgetTemplateResponse(
            template.getId(),
            template.getCode(),
            template.getName(),
            template.getDescription(),
            template.getIndustryType(),
            template.getActive(),
            lines
        );
    }
}
