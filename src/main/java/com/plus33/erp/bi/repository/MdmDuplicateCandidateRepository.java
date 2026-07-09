/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : MdmDuplicateCandidateRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MdmDuplicateCandidateController
 * Related Service   : MdmDuplicateCandidateService, MdmDuplicateCandidateServiceImpl
 * Related Repository: MdmDuplicateCandidateRepository
 * Related Entity    : MdmDuplicateCandidate
 * Related DTO       : N/A
 * Related Mapper    : MdmDuplicateCandidateMapper
 * Related DB Table  : mdm_duplicate_candidates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MdmDuplicateCandidateService, MdmDuplicateCandidateServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'mdm_duplicate_candidates' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.MdmDuplicateCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code MdmDuplicateCandidateRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'mdm_duplicate_candidates' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code mdm_duplicate_candidates}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface MdmDuplicateCandidateRepository extends JpaRepository<MdmDuplicateCandidate, Long> {
}