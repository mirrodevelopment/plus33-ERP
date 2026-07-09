/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.repository
 * File              : ProjectCostRepository.java
 * Purpose           : JPA Repository providing database CRUD for Project Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectCostController
 * Related Service   : ProjectCostService, ProjectCostServiceImpl
 * Related Repository: ProjectCostRepository
 * Related Entity    : ProjectCost
 * Related DTO       : N/A
 * Related Mapper    : ProjectCostMapper
 * Related DB Table  : project_costs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectCostService, ProjectCostServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Project Module against the 'project_costs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectCost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectCostRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'project_costs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code project_costs}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProjectCostRepository extends JpaRepository<ProjectCost, Long> {
    List<ProjectCost> findByProjectId(Long projectId);
}
