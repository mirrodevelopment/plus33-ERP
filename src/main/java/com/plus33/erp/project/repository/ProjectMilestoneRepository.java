/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.repository
 * File              : ProjectMilestoneRepository.java
 * Purpose           : JPA Repository providing database CRUD for Project Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectMilestoneController
 * Related Service   : ProjectMilestoneService, ProjectMilestoneServiceImpl
 * Related Repository: ProjectMilestoneRepository
 * Related Entity    : ProjectMilestone
 * Related DTO       : N/A
 * Related Mapper    : ProjectMilestoneMapper
 * Related DB Table  : project_milestones
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectMilestoneService, ProjectMilestoneServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Project Module against the 'project_milestones' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectMilestone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMilestoneRepository extends JpaRepository<ProjectMilestone, Long> {
}
