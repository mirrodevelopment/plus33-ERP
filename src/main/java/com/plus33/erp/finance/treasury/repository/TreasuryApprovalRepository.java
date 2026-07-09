/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : TreasuryApprovalRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryApprovalController
 * Related Service   : TreasuryApprovalService, TreasuryApprovalServiceImpl
 * Related Repository: TreasuryApprovalRepository
 * Related Entity    : TreasuryApproval
 * Related DTO       : N/A
 * Related Mapper    : TreasuryApprovalMapper
 * Related DB Table  : treasury_approvals
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TreasuryApprovalService, TreasuryApprovalServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'treasury_approvals' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.TreasuryApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryApprovalRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'treasury_approvals' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_approvals}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TreasuryApprovalRepository extends JpaRepository<TreasuryApproval, Long> {
    List<TreasuryApproval> findByTransferId(Long transferId);
    List<TreasuryApproval> findByPaymentBatchId(Long paymentBatchId);
}