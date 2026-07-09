/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : CycleCountPlanRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CycleCountPlanController
 * Related Service   : CycleCountPlanService, CycleCountPlanServiceImpl
 * Related Repository: CycleCountPlanRepository
 * Related Entity    : CycleCountPlan
 * Related DTO       : N/A
 * Related Mapper    : CycleCountPlanMapper
 * Related DB Table  : cycle_count_plans
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CycleCountPlanService, CycleCountPlanServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'cycle_count_plans' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.CycleCountPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code CycleCountPlanRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'cycle_count_plans' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code cycle_count_plans}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CycleCountPlanRepository extends JpaRepository<CycleCountPlan, Long> {
    Optional<CycleCountPlan> findByCompanyIdAndPlanNumber(Long companyId, String planNumber);
    List<CycleCountPlan> findByCompanyIdAndWarehouseIdAndStatus(Long companyId, Long warehouseId, String status);
}
