/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : RiskAssessmentRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RiskAssessmentController
 * Related Service   : RiskAssessmentService, RiskAssessmentServiceImpl
 * Related Repository: RiskAssessmentRepository
 * Related Entity    : RiskAssessment
 * Related DTO       : N/A
 * Related Mapper    : RiskAssessmentMapper
 * Related DB Table  : risk_assessments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RiskAssessmentService, RiskAssessmentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'risk_assessments' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;

import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code RiskAssessmentRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'risk_assessments' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code risk_assessments}</p>
 * <p><b>Module Deps      :</b> Grc</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, Long> {
    List<RiskAssessment> findByRiskId(Long riskId);
}

