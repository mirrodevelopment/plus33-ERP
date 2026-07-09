/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.repository
 * File              : BudgetDimensionSetRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetDimensionSetController
 * Related Service   : BudgetDimensionSetService, BudgetDimensionSetServiceImpl
 * Related Repository: BudgetDimensionSetRepository
 * Related Entity    : BudgetDimensionSet
 * Related DTO       : N/A
 * Related Mapper    : BudgetDimensionSetMapper
 * Related DB Table  : budget_dimension_sets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetDimensionSetService, BudgetDimensionSetServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'budget_dimension_sets' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetDimensionSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetDimensionSetRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'budget_dimension_sets' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_dimension_sets}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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