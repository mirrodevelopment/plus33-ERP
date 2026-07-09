/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.repository
 * File              : PreventiveMaintenancePlanRepository.java
 * Purpose           : JPA Repository providing database CRUD for Esm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PreventiveMaintenancePlanController
 * Related Service   : PreventiveMaintenancePlanService, PreventiveMaintenancePlanServiceImpl
 * Related Repository: PreventiveMaintenancePlanRepository
 * Related Entity    : PreventiveMaintenancePlan
 * Related DTO       : N/A
 * Related Mapper    : PreventiveMaintenancePlanMapper
 * Related DB Table  : preventive_maintenance_plans
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PreventiveMaintenancePlanService, PreventiveMaintenancePlanServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Esm Module against the 'preventive_maintenance_plans' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.PreventiveMaintenancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code PreventiveMaintenancePlanRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'preventive_maintenance_plans' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code preventive_maintenance_plans}</p>
 * <p><b>Module Deps      :</b> Esm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PreventiveMaintenancePlanRepository extends JpaRepository<PreventiveMaintenancePlan, Long> {
    List<PreventiveMaintenancePlan> findByActive(Boolean active);
}
