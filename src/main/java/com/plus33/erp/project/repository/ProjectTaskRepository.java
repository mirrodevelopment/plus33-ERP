/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.repository
 * File              : ProjectTaskRepository.java
 * Purpose           : JPA Repository providing database CRUD for Project Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectTaskController
 * Related Service   : ProjectTaskService, ProjectTaskServiceImpl
 * Related Repository: ProjectTaskRepository
 * Related Entity    : ProjectTask
 * Related DTO       : N/A
 * Related Mapper    : ProjectTaskMapper
 * Related DB Table  : project_tasks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectTaskService, ProjectTaskServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Project Module against the 'project_tasks' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectTaskRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'project_tasks' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code project_tasks}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {
    List<ProjectTask> findByWbsVersionId(Long wbsVersionId);
}
