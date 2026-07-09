/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.repository
 * File              : ProjectRiskRepository.java
 * Purpose           : JPA Repository providing database CRUD for Project Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectRiskController
 * Related Service   : ProjectRiskService, ProjectRiskServiceImpl
 * Related Repository: ProjectRiskRepository
 * Related Entity    : ProjectRisk
 * Related DTO       : N/A
 * Related Mapper    : ProjectRiskMapper
 * Related DB Table  : project_risks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectRiskService, ProjectRiskServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Project Module against the 'project_risks' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectRisk;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectRiskRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'project_risks' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code project_risks}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProjectRiskRepository extends JpaRepository<ProjectRisk, Long> {
    List<ProjectRisk> findByProjectId(Long projectId);
}
