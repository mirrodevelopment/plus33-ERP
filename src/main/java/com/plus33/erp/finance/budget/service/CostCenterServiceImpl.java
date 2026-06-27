package com.plus33.erp.finance.budget.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.finance.budget.dto.CostCenterRequest;
import com.plus33.erp.finance.budget.dto.CostCenterResponse;
import com.plus33.erp.finance.budget.entity.CostCenter;
import com.plus33.erp.finance.budget.repository.CostCenterRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CostCenterServiceImpl implements CostCenterService {

    private final CostCenterRepository costCenterRepository;
    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public CostCenterResponse createCostCenter(Long companyId, CostCenterRequest request) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new BusinessException("Company not found"));

        if (costCenterRepository.findByCompanyIdAndCode(companyId, request.code()).isPresent()) {
            throw new BusinessException("Cost Center code already exists: " + request.code());
        }

        CostCenter costCenter = CostCenter.builder()
            .company(company)
            .code(request.code())
            .name(request.name())
            .active(request.active() != null ? request.active() : true)
            .build();

        return mapToResponse(costCenterRepository.save(costCenter));
    }

    @Override
    @Transactional
    public CostCenterResponse updateCostCenter(Long id, CostCenterRequest request) {
        CostCenter costCenter = costCenterRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Cost Center not found"));

        costCenter.setName(request.name());
        if (request.active() != null) {
            costCenter.setActive(request.active());
        }

        return mapToResponse(costCenterRepository.save(costCenter));
    }

    @Override
    @Transactional(readOnly = true)
    public CostCenterResponse getCostCenter(Long id) {
        CostCenter costCenter = costCenterRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Cost Center not found"));
        return mapToResponse(costCenter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CostCenterResponse> getCostCentersByCompany(Long companyId) {
        return costCenterRepository.findAll().stream()
            .filter(c -> c.getCompany().getId().equals(companyId))
            .map(this::mapToResponse)
            .toList();
    }

    private CostCenterResponse mapToResponse(CostCenter costCenter) {
        return new CostCenterResponse(
            costCenter.getId(),
            costCenter.getCompany().getId(),
            costCenter.getCode(),
            costCenter.getName(),
            costCenter.getActive()
        );
    }
}
