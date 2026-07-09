/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : LearningEnrollmentRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LearningEnrollmentController
 * Related Service   : LearningEnrollmentService, LearningEnrollmentServiceImpl
 * Related Repository: LearningEnrollmentRepository
 * Related Entity    : LearningEnrollment
 * Related DTO       : N/A
 * Related Mapper    : LearningEnrollmentMapper
 * Related DB Table  : learning_enrollments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : LearningEnrollmentService, LearningEnrollmentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'learning_enrollments' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.LearningEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code LearningEnrollmentRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'learning_enrollments' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code learning_enrollments}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface LearningEnrollmentRepository extends JpaRepository<LearningEnrollment, Long> {
    List<LearningEnrollment> findByEmployeeId(Long employeeId);
}
