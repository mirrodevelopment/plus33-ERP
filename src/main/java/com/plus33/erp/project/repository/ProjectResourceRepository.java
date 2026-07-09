/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.repository
 * File              : ProjectResourceRepository.java
 * Purpose           : JPA Repository providing database CRUD for Project Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectResourceController
 * Related Service   : ProjectResourceService, ProjectResourceServiceImpl
 * Related Repository: ProjectResourceRepository
 * Related Entity    : ProjectResource
 * Related DTO       : N/A
 * Related Mapper    : ProjectResourceMapper
 * Related DB Table  : project_resources
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectResourceService, ProjectResourceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Project Module against the 'project_resources' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectResource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectResourceRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'project_resources' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code project_resources}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProjectResourceRepository extends JpaRepository<ProjectResource, Long> {
    Optional<ProjectResource> findByEmail(String email);
}
