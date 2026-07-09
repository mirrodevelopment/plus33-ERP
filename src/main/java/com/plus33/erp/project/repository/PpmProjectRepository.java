/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.repository
 * File              : PpmProjectRepository.java
 * Purpose           : JPA Repository providing database CRUD for Project Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PpmProjectController
 * Related Service   : PpmProjectService, PpmProjectServiceImpl
 * Related Repository: PpmProjectRepository
 * Related Entity    : PpmProject
 * Related DTO       : N/A
 * Related Mapper    : PpmProjectMapper
 * Related DB Table  : ppm_projects
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PpmProjectService, PpmProjectServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Project Module against the 'ppm_projects' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.PpmProject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code PpmProjectRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'ppm_projects' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code ppm_projects}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PpmProjectRepository extends JpaRepository<PpmProject, Long> {
    Optional<PpmProject> findByProjectNumber(String projectNumber);
}
