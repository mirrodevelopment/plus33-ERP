/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.repository
 * File              : ProjectWbsRepository.java
 * Purpose           : JPA Repository providing database CRUD for Project Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectWbsController
 * Related Service   : ProjectWbsService, ProjectWbsServiceImpl
 * Related Repository: ProjectWbsRepository
 * Related Entity    : ProjectWbs
 * Related DTO       : N/A
 * Related Mapper    : ProjectWbsMapper
 * Related DB Table  : project_wbss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectWbsService, ProjectWbsServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Project Module against the 'project_wbss' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectWbs;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectWbsRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'project_wbss' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code project_wbss}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProjectWbsRepository extends JpaRepository<ProjectWbs, Long> {
    Optional<ProjectWbs> findByProjectId(Long projectId);
}
