/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : CycleCountTaskRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CycleCountTaskController
 * Related Service   : CycleCountTaskService, CycleCountTaskServiceImpl
 * Related Repository: CycleCountTaskRepository
 * Related Entity    : CycleCountTask
 * Related DTO       : N/A
 * Related Mapper    : CycleCountTaskMapper
 * Related DB Table  : cycle_count_tasks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CycleCountTaskService, CycleCountTaskServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'cycle_count_tasks' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.CycleCountTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code CycleCountTaskRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'cycle_count_tasks' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code cycle_count_tasks}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CycleCountTaskRepository extends JpaRepository<CycleCountTask, Long> {
    List<CycleCountTask> findByPlan_Id(Long planId);
    List<CycleCountTask> findByPlan_IdAndStatus(Long planId, String status);
    List<CycleCountTask> findByAssignedToAndStatus(Long userId, String status);
    List<CycleCountTask> findByLocation_IdAndStatus(Long locationId, String status);
}
