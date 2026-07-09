/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : MdmStewardAssignmentRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MdmStewardAssignmentController
 * Related Service   : MdmStewardAssignmentService, MdmStewardAssignmentServiceImpl
 * Related Repository: MdmStewardAssignmentRepository
 * Related Entity    : MdmStewardAssignment
 * Related DTO       : N/A
 * Related Mapper    : MdmStewardAssignmentMapper
 * Related DB Table  : mdm_steward_assignments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MdmStewardAssignmentService, MdmStewardAssignmentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'mdm_steward_assignments' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.MdmStewardAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code MdmStewardAssignmentRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'mdm_steward_assignments' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code mdm_steward_assignments}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface MdmStewardAssignmentRepository extends JpaRepository<MdmStewardAssignment, Long> {
}