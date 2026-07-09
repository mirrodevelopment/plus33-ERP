/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : ReplenishmentTaskRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentTaskController
 * Related Service   : ReplenishmentTaskService, ReplenishmentTaskServiceImpl
 * Related Repository: ReplenishmentTaskRepository
 * Related Entity    : ReplenishmentTask
 * Related DTO       : N/A
 * Related Mapper    : ReplenishmentTaskMapper
 * Related DB Table  : replenishment_tasks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ReplenishmentTaskService, ReplenishmentTaskServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'replenishment_tasks' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.ReplenishmentTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code ReplenishmentTaskRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'replenishment_tasks' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code replenishment_tasks}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ReplenishmentTaskRepository extends JpaRepository<ReplenishmentTask, Long> {
    List<ReplenishmentTask> findByCompanyIdAndWarehouseIdAndStatus(Long companyId, Long warehouseId, String status);
    List<ReplenishmentTask> findByAssignedToAndStatus(Long userId, String status);
}
