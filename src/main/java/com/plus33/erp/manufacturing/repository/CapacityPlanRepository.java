/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : CapacityPlanRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CapacityPlanController
 * Related Service   : CapacityPlanService, CapacityPlanServiceImpl
 * Related Repository: CapacityPlanRepository
 * Related Entity    : CapacityPlan
 * Related DTO       : N/A
 * Related Mapper    : CapacityPlanMapper
 * Related DB Table  : capacity_plans
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CapacityPlanService, CapacityPlanServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'capacity_plans' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.CapacityPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code CapacityPlanRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'capacity_plans' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code capacity_plans}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CapacityPlanRepository extends JpaRepository<CapacityPlan, Long> {
    List<CapacityPlan> findByMrpRunId(Long mrpRunId);
    List<CapacityPlan> findByWorkCenterIdAndPlanningDate(Long workCenterId, LocalDate planningDate);
    List<CapacityPlan> findByWorkCenterIdAndPlanningDateBetween(Long workCenterId, LocalDate start, LocalDate end);
}
