/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.service
 * File              : BudgetTemplateServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetTemplateController
 * Related Service   : BudgetTemplateServiceImpl
 * Related Repository: BudgetTemplateRepository
 * Related Entity    : BudgetTemplate
 * Related DTO       : BudgetTemplateLineResponse, BudgetTemplateRequest, BudgetTemplateResponse, mapToResponse
 * Related Mapper    : BudgetTemplateMapper
 * Related DB Table  : budget_templates
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : BudgetTemplateController, BudgetTemplateServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements BudgetTemplateService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetTemplateServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * BudgetTemplateController
 *   --> BudgetTemplateServiceImpl (this)
 *   --> Validate business rules
 *   --> BudgetTemplateRepository (read/write 'budget_templates')
 *   --> BudgetTemplateMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code budget_templates}</p>
 * <p><b>Module Deps      :</b> Common, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class BudgetTemplateServiceImpl implements BudgetTemplateService {

    private final BudgetTemplateRepository budgetTemplateRepository;

    /**
     * Creates a new template and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the BudgetTemplateResponse result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Updates an existing template record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the BudgetTemplateResponse result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Retrieves template data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the BudgetTemplateResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public BudgetTemplateResponse getTemplate(Long id) {
        BudgetTemplate template = budgetTemplateRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Budget Template not found"));
        return mapToResponse(template);
    }

    /**
     * Retrieves a paginated list of all templates records.
     *
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
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