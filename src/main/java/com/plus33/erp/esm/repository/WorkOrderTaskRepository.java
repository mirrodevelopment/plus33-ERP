/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.repository
 * File              : WorkOrderTaskRepository.java
 * Purpose           : JPA Repository providing database CRUD for Esm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WorkOrderTaskController
 * Related Service   : WorkOrderTaskService, WorkOrderTaskServiceImpl
 * Related Repository: WorkOrderTaskRepository
 * Related Entity    : WorkOrderTask
 * Related DTO       : N/A
 * Related Mapper    : WorkOrderTaskMapper
 * Related DB Table  : work_order_tasks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WorkOrderTaskService, WorkOrderTaskServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Esm Module against the 'work_order_tasks' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.WorkOrderTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code WorkOrderTaskRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'work_order_tasks' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code work_order_tasks}</p>
 * <p><b>Module Deps      :</b> Esm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface WorkOrderTaskRepository extends JpaRepository<WorkOrderTask, Long> {
    List<WorkOrderTask> findByWorkOrderId(Long workOrderId);
}
