/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : TreasuryApprovalStepRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryApprovalStepController
 * Related Service   : TreasuryApprovalStepService, TreasuryApprovalStepServiceImpl
 * Related Repository: TreasuryApprovalStepRepository
 * Related Entity    : TreasuryApprovalStep
 * Related DTO       : N/A
 * Related Mapper    : TreasuryApprovalStepMapper
 * Related DB Table  : treasury_approval_steps
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TreasuryApprovalStepService, TreasuryApprovalStepServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'treasury_approval_steps' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.TreasuryApprovalStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryApprovalStepRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'treasury_approval_steps' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_approval_steps}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TreasuryApprovalStepRepository extends JpaRepository<TreasuryApprovalStep, Long> {
    List<TreasuryApprovalStep> findByActiveTrueOrderByStepSequenceAsc();
}