/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.repository
 * File              : ProjectChangeRequestRepository.java
 * Purpose           : JPA Repository providing database CRUD for Project Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectChangeRequestController
 * Related Service   : ProjectChangeRequestService, ProjectChangeRequestServiceImpl
 * Related Repository: ProjectChangeRequestRepository
 * Related Entity    : ProjectChangeRequest
 * Related DTO       : ProjectChangeRequest
 * Related Mapper    : ProjectChangeRequestMapper
 * Related DB Table  : project_change_requests
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectChangeRequestService, ProjectChangeRequestServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Project Module against the 'project_change_requests' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectChangeRequestRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'project_change_requests' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code project_change_requests}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProjectChangeRequestRepository extends JpaRepository<ProjectChangeRequest, Long> {
    Optional<ProjectChangeRequest> findByRequestNumber(String requestNumber);
}
