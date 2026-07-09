/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : SuccessorRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SuccessorController
 * Related Service   : SuccessorService, SuccessorServiceImpl
 * Related Repository: SuccessorRepository
 * Related Entity    : Successor
 * Related DTO       : N/A
 * Related Mapper    : SuccessorMapper
 * Related DB Table  : successors
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SuccessorService, SuccessorServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'successors' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.Successor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code SuccessorRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'successors' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code successors}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface SuccessorRepository extends JpaRepository<Successor, Long> {
    List<Successor> findByTalentPoolId(Long talentPoolId);
}
