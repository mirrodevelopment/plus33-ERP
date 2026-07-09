/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : RobotTaskRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RobotTaskController
 * Related Service   : RobotTaskService, RobotTaskServiceImpl
 * Related Repository: RobotTaskRepository
 * Related Entity    : RobotTask
 * Related DTO       : N/A
 * Related Mapper    : RobotTaskMapper
 * Related DB Table  : robot_tasks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RobotTaskService, RobotTaskServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'robot_tasks' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.RobotTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code RobotTaskRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'robot_tasks' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code robot_tasks}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface RobotTaskRepository extends JpaRepository<RobotTask, Long> {
    List<RobotTask> findByStatus(String status);
}
