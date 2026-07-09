/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : PositionAssignmentRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PositionAssignmentController
 * Related Service   : PositionAssignmentService, PositionAssignmentServiceImpl
 * Related Repository: PositionAssignmentRepository
 * Related Entity    : PositionAssignment
 * Related DTO       : N/A
 * Related Mapper    : PositionAssignmentMapper
 * Related DB Table  : position_assignments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PositionAssignmentService, PositionAssignmentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'position_assignments' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.PositionAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code PositionAssignmentRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'position_assignments' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code position_assignments}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PositionAssignmentRepository extends JpaRepository<PositionAssignment, Long> {
    List<PositionAssignment> findByPositionIdAndIsCurrent(Long positionId, Boolean isCurrent);
}
