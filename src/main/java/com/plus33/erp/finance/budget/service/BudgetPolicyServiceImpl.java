/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.service
 * File              : BudgetPolicyServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetPolicyController
 * Related Service   : BudgetPolicyServiceImpl
 * Related Repository: BudgetPolicyRepository, CompanyRepository
 * Related Entity    : BudgetPolicy
 * Related DTO       : BudgetPolicyRequest, BudgetPolicyResponse, mapToResponse
 * Related Mapper    : BudgetPolicyMapper
 * Related DB Table  : budget_policys
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module
 * Used By           : BudgetPolicyController, BudgetPolicyServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements BudgetPolicyService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.budget.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.finance.budget.dto.BudgetPolicyRequest;
import com.plus33.erp.finance.budget.dto.BudgetPolicyResponse;
import com.plus33.erp.finance.budget.entity.BudgetPolicy;
import com.plus33.erp.finance.budget.repository.BudgetPolicyRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetPolicyServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * BudgetPolicyController
 *   --> BudgetPolicyServiceImpl (this)
 *   --> Validate business rules
 *   --> BudgetPolicyRepository (read/write 'budget_policys')
 *   --> BudgetPolicyMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code budget_policys}</p>
 * <p><b>Module Deps      :</b> Common, Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class BudgetPolicyServiceImpl implements BudgetPolicyService {

    private final BudgetPolicyRepository budgetPolicyRepository;
    private final CompanyRepository companyRepository;

    /**
     * Creates a new policy and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param request the validated request DTO containing input data
     * @return the BudgetPolicyResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public BudgetPolicyResponse createPolicy(Long companyId, BudgetPolicyRequest request) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new BusinessException("Company not found"));

        if (budgetPolicyRepository.findByCompanyIdAndCode(companyId, request.code()).isPresent()) {
            throw new BusinessException("Budget Policy code already exists: " + request.code());
        }

        BudgetPolicy policy = BudgetPolicy.builder()
            .company(company)
            .code(request.code())
            .name(request.name())
            .controlType(request.controlType() != null ? request.controlType() : "HARD")
            .allowNegative(request.allowNegative() != null ? request.allowNegative() : false)
            .allowTransfers(request.allowTransfers() != null ? request.allowTransfers() : true)
            .allowRevisions(request.allowRevisions() != null ? request.allowRevisions() : true)
            .autoReserve(request.autoReserve() != null ? request.autoReserve() : true)
            .autoConsume(request.autoConsume() != null ? request.autoConsume() : true)
            .approvalRequired(request.approvalRequired() != null ? request.approvalRequired() : true)
            .multiCurrencyEnabled(request.multiCurrencyEnabled() != null ? request.multiCurrencyEnabled() : false)
            .active(request.active() != null ? request.active() : true)
            .build();

        return mapToResponse(budgetPolicyRepository.save(policy));
    }

    /**
     * Updates an existing policy record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the BudgetPolicyResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public BudgetPolicyResponse updatePolicy(Long id, BudgetPolicyRequest request) {
        BudgetPolicy policy = budgetPolicyRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Budget Policy not found"));

        policy.setName(request.name());
        if (request.controlType() != null) policy.setControlType(request.controlType());
        if (request.allowNegative() != null) policy.setAllowNegative(request.allowNegative());
        if (request.allowTransfers() != null) policy.setAllowTransfers(request.allowTransfers());
        if (request.allowRevisions() != null) policy.setAllowRevisions(request.allowRevisions());
        if (request.autoReserve() != null) policy.setAutoReserve(request.autoReserve());
        if (request.autoConsume() != null) policy.setAutoConsume(request.autoConsume());
        if (request.approvalRequired() != null) policy.setApprovalRequired(request.approvalRequired());
        if (request.multiCurrencyEnabled() != null) policy.setMultiCurrencyEnabled(request.multiCurrencyEnabled());
        if (request.active() != null) policy.setActive(request.active());

        return mapToResponse(budgetPolicyRepository.save(policy));
    }

    /**
     * Retrieves policy data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the BudgetPolicyResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public BudgetPolicyResponse getPolicy(Long id) {
        BudgetPolicy policy = budgetPolicyRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Budget Policy not found"));
        return mapToResponse(policy);
    }

    /**
     * Retrieves policies by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<BudgetPolicyResponse> getPoliciesByCompany(Long companyId) {
        return budgetPolicyRepository.findAll().stream()
            .filter(p -> p.getCompany().getId().equals(companyId))
            .map(this::mapToResponse)
            .toList();
    }

    private BudgetPolicyResponse mapToResponse(BudgetPolicy policy) {
        return new BudgetPolicyResponse(
            policy.getId(),
            policy.getCompany().getId(),
            policy.getCode(),
            policy.getName(),
            policy.getControlType(),
            policy.getAllowNegative(),
            policy.getAllowTransfers(),
            policy.getAllowRevisions(),
            policy.getAutoReserve(),
            policy.getAutoConsume(),
            policy.getApprovalRequired(),
            policy.getMultiCurrencyEnabled(),
            policy.getActive()
        );
    }
}