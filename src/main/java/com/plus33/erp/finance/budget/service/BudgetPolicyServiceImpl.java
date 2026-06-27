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

@Service
@RequiredArgsConstructor
public class BudgetPolicyServiceImpl implements BudgetPolicyService {

    private final BudgetPolicyRepository budgetPolicyRepository;
    private final CompanyRepository companyRepository;

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

    @Override
    @Transactional(readOnly = true)
    public BudgetPolicyResponse getPolicy(Long id) {
        BudgetPolicy policy = budgetPolicyRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Budget Policy not found"));
        return mapToResponse(policy);
    }

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
