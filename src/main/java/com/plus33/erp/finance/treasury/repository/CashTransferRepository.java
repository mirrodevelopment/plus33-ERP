/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : CashTransferRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CashTransferController
 * Related Service   : CashTransferService, CashTransferServiceImpl
 * Related Repository: CashTransferRepository
 * Related Entity    : CashTransfer
 * Related DTO       : N/A
 * Related Mapper    : CashTransferMapper
 * Related DB Table  : cash_transfers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CashTransferService, CashTransferServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'cash_transfers' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.CashTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code CashTransferRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'cash_transfers' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code cash_transfers}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface CashTransferRepository extends JpaRepository<CashTransfer, Long> {
    List<CashTransfer> findByCompanyId(Long companyId);
    List<CashTransfer> findByStatus(String status);
}