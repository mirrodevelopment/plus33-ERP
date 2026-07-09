/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.repository
 * File              : FixedAssetMaintenancePlanRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetMaintenancePlanController
 * Related Service   : FixedAssetMaintenancePlanService, FixedAssetMaintenancePlanServiceImpl
 * Related Repository: FixedAssetMaintenancePlanRepository
 * Related Entity    : FixedAssetMaintenancePlan
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetMaintenancePlanMapper
 * Related DB Table  : fixed_asset_maintenance_plans
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetMaintenancePlanService, FixedAssetMaintenancePlanServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'fixed_asset_maintenance_plans' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetMaintenancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetMaintenancePlanRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'fixed_asset_maintenance_plans' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_maintenance_plans}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface FixedAssetMaintenancePlanRepository extends JpaRepository<FixedAssetMaintenancePlan, Long> {
    List<FixedAssetMaintenancePlan> findAllByFixedAssetId(Long fixedAssetId);
    List<FixedAssetMaintenancePlan> findAllByFixedAssetIdAndActiveTrue(Long fixedAssetId);
    List<FixedAssetMaintenancePlan> findAllByActiveTrueAndNextMaintenanceDateLessThanEqual(LocalDate date);
}