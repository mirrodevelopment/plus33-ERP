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
