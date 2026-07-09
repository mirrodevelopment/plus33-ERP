/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.repository
 * File              : ResourceAssignmentRepository.java
 * Purpose           : JPA Repository providing database CRUD for Project Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ResourceAssignmentController
 * Related Service   : ResourceAssignmentService, ResourceAssignmentServiceImpl
 * Related Repository: ResourceAssignmentRepository
 * Related Entity    : ResourceAssignment
 * Related DTO       : N/A
 * Related Mapper    : ResourceAssignmentMapper
 * Related DB Table  : resource_assignments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ResourceAssignmentService, ResourceAssignmentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Project Module against the 'resource_assignments' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ResourceAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ResourceAssignmentRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'resource_assignments' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code resource_assignments}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ResourceAssignmentRepository extends JpaRepository<ResourceAssignment, Long> {
    List<ResourceAssignment> findByResourceId(Long resourceId);
    List<ResourceAssignment> findByTaskId(Long taskId);
}
