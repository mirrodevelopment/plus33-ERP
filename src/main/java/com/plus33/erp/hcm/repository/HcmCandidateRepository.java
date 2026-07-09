/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : HcmCandidateRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: HcmCandidateController
 * Related Service   : HcmCandidateService, HcmCandidateServiceImpl
 * Related Repository: HcmCandidateRepository
 * Related Entity    : HcmCandidate
 * Related DTO       : N/A
 * Related Mapper    : HcmCandidateMapper
 * Related DB Table  : hcm_candidates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : HcmCandidateService, HcmCandidateServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'hcm_candidates' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.HcmCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code HcmCandidateRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'hcm_candidates' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code hcm_candidates}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface HcmCandidateRepository extends JpaRepository<HcmCandidate, Long> {
    List<HcmCandidate> findByRequisitionId(Long requisitionId);
}
