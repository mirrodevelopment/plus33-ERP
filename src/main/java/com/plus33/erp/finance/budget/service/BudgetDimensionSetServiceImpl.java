/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.service
 * File              : BudgetDimensionSetServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetDimensionSetController
 * Related Service   : BudgetDimensionSetServiceImpl
 * Related Repository: CompanyRepository, DepartmentRepository, CostCenterRepository, ProjectRepository, WarehouseRepository, AssetCategoryRepository, RegionRepository, StoreRepository, BudgetDimensionSetRepository
 * Related Entity    : BudgetDimensionSet
 * Related DTO       : BudgetDimensionSetRequest
 * Related Mapper    : BudgetDimensionSetMapper
 * Related DB Table  : budget_dimension_sets
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module
 * Used By           : BudgetDimensionSetController, BudgetDimensionSetServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements BudgetDimensionSetService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.budget.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.finance.budget.dto.BudgetDimensionSetRequest;
import com.plus33.erp.finance.budget.entity.BudgetDimensionSet;
import com.plus33.erp.finance.budget.repository.BudgetDimensionSetRepository;
import com.plus33.erp.finance.budget.repository.CostCenterRepository;
import com.plus33.erp.finance.budget.repository.DepartmentRepository;
import com.plus33.erp.finance.budget.repository.ProjectRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.RegionRepository;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
import com.plus33.erp.finance.assets.repository.AssetCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetDimensionSetServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * BudgetDimensionSetController
 *   --> BudgetDimensionSetServiceImpl (this)
 *   --> Validate business rules
 *   --> BudgetDimensionSetRepository (read/write 'budget_dimension_sets')
 *   --> BudgetDimensionSetMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code budget_dimension_sets}</p>
 * <p><b>Module Deps      :</b> Common, Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class BudgetDimensionSetServiceImpl implements BudgetDimensionSetService {

    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;
    private final CostCenterRepository costCenterRepository;
    private final ProjectRepository projectRepository;
    private final WarehouseRepository warehouseRepository;
    private final AssetCategoryRepository assetCategoryRepository;
    private final RegionRepository regionRepository;
    private final StoreRepository storeRepository;
    private final BudgetDimensionSetRepository budgetDimensionSetRepository;

    /**
     * Retrieves or create dimension set data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param request the validated request DTO containing input data
     * @return the BudgetDimensionSet result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional
    public BudgetDimensionSet getOrCreateDimensionSet(Long companyId, BudgetDimensionSetRequest request) {
        if (request == null) {
            return getOrCreateDimensionSet(companyId, null, null, null, null, null, null, null);
        }
        return getOrCreateDimensionSet(
            companyId,
            request.departmentId(),
            request.costCenterId(),
            request.projectId(),
            request.warehouseId(),
            request.assetCategoryId(),
            request.regionId(),
            request.storeId()
        );
    }

    /**
     * Retrieves or create dimension set data from the database.
     *
     * @return the BudgetDimensionSet result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional
    public BudgetDimensionSet getOrCreateDimensionSet(
            Long companyId,
            Long departmentId,
            Long costCenterId,
            Long projectId,
            Long warehouseId,
            Long assetCategoryId,
            Long regionId,
            Long storeId
    ) {
        return budgetDimensionSetRepository.findByDimensions(
            companyId, departmentId, costCenterId, projectId, warehouseId, assetCategoryId, regionId, storeId
        ).orElseGet(() -> {
            Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new BusinessException("Company not found: " + companyId));

            BudgetDimensionSet.BudgetDimensionSetBuilder builder = BudgetDimensionSet.builder().company(company);

            if (departmentId != null) {
                builder.department(departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new BusinessException("Department not found: " + departmentId)));
            }
            if (costCenterId != null) {
                builder.costCenter(costCenterRepository.findById(costCenterId)
                    .orElseThrow(() -> new BusinessException("Cost Center not found: " + costCenterId)));
            }
            if (projectId != null) {
                builder.project(projectRepository.findById(projectId)
                    .orElseThrow(() -> new BusinessException("Project not found: " + projectId)));
            }
            if (warehouseId != null) {
                builder.warehouse(warehouseRepository.findById(warehouseId)
                    .orElseThrow(() -> new BusinessException("Warehouse not found: " + warehouseId)));
            }
            if (assetCategoryId != null) {
                builder.assetCategory(assetCategoryRepository.findById(assetCategoryId)
                    .orElseThrow(() -> new BusinessException("Asset Category not found: " + assetCategoryId)));
            }
            if (regionId != null) {
                builder.region(regionRepository.findById(regionId)
                    .orElseThrow(() -> new BusinessException("Region not found: " + regionId)));
            }
            if (storeId != null) {
                builder.store(storeRepository.findById(storeId)
                    .orElseThrow(() -> new BusinessException("Store not found: " + storeId)));
            }

            return budgetDimensionSetRepository.save(builder.build());
        });
    }
}