/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : ReplenishmentPlanRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentPlanController
 * Related Service   : ReplenishmentPlanService, ReplenishmentPlanServiceImpl
 * Related Repository: ReplenishmentPlanRepository
 * Related Entity    : ReplenishmentPlan
 * Related DTO       : N/A
 * Related Mapper    : ReplenishmentPlanMapper
 * Related DB Table  : replenishment_plans
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ReplenishmentPlanService, ReplenishmentPlanServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'replenishment_plans' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.ReplenishmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code ReplenishmentPlanRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'replenishment_plans' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code replenishment_plans}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ReplenishmentPlanRepository extends JpaRepository<ReplenishmentPlan, Long> {
    List<ReplenishmentPlan> findByCompanyIdAndWarehouseIdAndActiveTrue(Long companyId, Long warehouseId);
    List<ReplenishmentPlan> findByWarehouseIdAndProductIdAndActiveTrue(Long warehouseId, Long productId);
}
