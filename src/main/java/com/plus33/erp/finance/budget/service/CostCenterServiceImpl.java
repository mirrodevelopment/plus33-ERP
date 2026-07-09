/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.service
 * File              : CostCenterServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CostCenterController
 * Related Service   : CostCenterServiceImpl
 * Related Repository: CostCenterRepository, CompanyRepository
 * Related Entity    : CostCenter
 * Related DTO       : CostCenterRequest, CostCenterResponse, mapToResponse
 * Related Mapper    : CostCenterMapper
 * Related DB Table  : cost_centers
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module
 * Used By           : CostCenterController, CostCenterServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements CostCenterService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code CostCenterServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CostCenterController
 *   --> CostCenterServiceImpl (this)
 *   --> Validate business rules
 *   --> CostCenterRepository (read/write 'cost_centers')
 *   --> CostCenterMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code cost_centers}</p>
 * <p><b>Module Deps      :</b> Common, Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class CostCenterServiceImpl implements CostCenterService {

    private final CostCenterRepository costCenterRepository;
    private final CompanyRepository companyRepository;

    /**
     * Creates a new cost center and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param request the validated request DTO containing input data
     * @return the CostCenterResponse result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Updates an existing cost center record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the CostCenterResponse result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Retrieves cost center data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the CostCenterResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public CostCenterResponse getCostCenter(Long id) {
        CostCenter costCenter = costCenterRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Cost Center not found"));
        return mapToResponse(costCenter);
    }

    /**
     * Retrieves cost centers by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
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