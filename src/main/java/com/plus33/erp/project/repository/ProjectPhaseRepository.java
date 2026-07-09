/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.repository
 * File              : ProjectPhaseRepository.java
 * Purpose           : JPA Repository providing database CRUD for Project Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectPhaseController
 * Related Service   : ProjectPhaseService, ProjectPhaseServiceImpl
 * Related Repository: ProjectPhaseRepository
 * Related Entity    : ProjectPhase
 * Related DTO       : N/A
 * Related Mapper    : ProjectPhaseMapper
 * Related DB Table  : project_phases
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectPhaseService, ProjectPhaseServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Project Module against the 'project_phases' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectPhase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectPhaseRepository extends JpaRepository<ProjectPhase, Long> {
}
