/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiGovernanceClassificationRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiGovernanceClassificationController
 * Related Service   : BiGovernanceClassificationService, BiGovernanceClassificationServiceImpl
 * Related Repository: BiGovernanceClassificationRepository
 * Related Entity    : BiGovernanceClassification
 * Related DTO       : N/A
 * Related Mapper    : BiGovernanceClassificationMapper
 * Related DB Table  : bi_governance_classifications
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiGovernanceClassificationService, BiGovernanceClassificationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_governance_classifications' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiGovernanceClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiGovernanceClassificationRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bi_governance_classifications' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_governance_classifications}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BiGovernanceClassificationRepository extends JpaRepository<BiGovernanceClassification, Long> {
}