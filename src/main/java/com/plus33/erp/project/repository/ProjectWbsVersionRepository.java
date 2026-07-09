/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.repository
 * File              : ProjectWbsVersionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Project Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectWbsVersionController
 * Related Service   : ProjectWbsVersionService, ProjectWbsVersionServiceImpl
 * Related Repository: ProjectWbsVersionRepository
 * Related Entity    : ProjectWbsVersion
 * Related DTO       : N/A
 * Related Mapper    : ProjectWbsVersionMapper
 * Related DB Table  : project_wbs_versions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectWbsVersionService, ProjectWbsVersionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Project Module against the 'project_wbs_versions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectWbsVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectWbsVersionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'project_wbs_versions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code project_wbs_versions}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProjectWbsVersionRepository extends JpaRepository<ProjectWbsVersion, Long> {
    List<ProjectWbsVersion> findByWbsId(Long wbsId);
}
