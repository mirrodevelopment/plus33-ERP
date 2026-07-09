/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : WarehouseTaskRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseTaskController
 * Related Service   : WarehouseTaskService, WarehouseTaskServiceImpl
 * Related Repository: WarehouseTaskRepository
 * Related Entity    : WarehouseTask
 * Related DTO       : N/A
 * Related Mapper    : WarehouseTaskMapper
 * Related DB Table  : warehouse_tasks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseTaskService, WarehouseTaskServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'warehouse_tasks' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseTaskRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'warehouse_tasks' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_tasks}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface WarehouseTaskRepository extends JpaRepository<WarehouseTask, Long> {
    List<WarehouseTask> findByCompanyIdAndWarehouseIdAndTaskStatus(Long companyId, Long warehouseId, String status);
    List<WarehouseTask> findByAssignedToAndTaskStatus(Long userId, String status);
    List<WarehouseTask> findByTaskTypeAndTaskStatus(String taskType, String status);
    List<WarehouseTask> findByRefTypeAndRefId(String refType, Long refId);
}
