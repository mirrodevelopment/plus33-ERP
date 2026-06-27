package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetDimensionSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetDimensionSetRepository extends JpaRepository<BudgetDimensionSet, Long> {

    @Query("SELECT d FROM BudgetDimensionSet d WHERE d.company.id = :companyId " +
           "AND (:departmentId IS NULL AND d.department IS NULL OR d.department.id = :departmentId) " +
           "AND (:costCenterId IS NULL AND d.costCenter IS NULL OR d.costCenter.id = :costCenterId) " +
           "AND (:projectId IS NULL AND d.project IS NULL OR d.project.id = :projectId) " +
           "AND (:warehouseId IS NULL AND d.warehouse IS NULL OR d.warehouse.id = :warehouseId) " +
           "AND (:assetCategoryId IS NULL AND d.assetCategory IS NULL OR d.assetCategory.id = :assetCategoryId) " +
           "AND (:regionId IS NULL AND d.region IS NULL OR d.region.id = :regionId) " +
           "AND (:storeId IS NULL AND d.store IS NULL OR d.store.id = :storeId)")
    Optional<BudgetDimensionSet> findByDimensions(
        @Param("companyId") Long companyId,
        @Param("departmentId") Long departmentId,
        @Param("costCenterId") Long costCenterId,
        @Param("projectId") Long projectId,
        @Param("warehouseId") Long warehouseId,
        @Param("assetCategoryId") Long assetCategoryId,
        @Param("regionId") Long regionId,
        @Param("storeId") Long storeId
    );
}
