/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.repository
 * File              : ProjectProgramRepository.java
 * Purpose           : JPA Repository providing database CRUD for Project Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectProgramController
 * Related Service   : ProjectProgramService, ProjectProgramServiceImpl
 * Related Repository: ProjectProgramRepository
 * Related Entity    : ProjectProgram
 * Related DTO       : N/A
 * Related Mapper    : ProjectProgramMapper
 * Related DB Table  : project_programs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectProgramService, ProjectProgramServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Project Module against the 'project_programs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectProgram;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectProgramRepository extends JpaRepository<ProjectProgram, Long> {
}
