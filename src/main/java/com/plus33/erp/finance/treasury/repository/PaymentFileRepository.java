/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : PaymentFileRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentFileController
 * Related Service   : PaymentFileService, PaymentFileServiceImpl
 * Related Repository: PaymentFileRepository
 * Related Entity    : PaymentFile
 * Related DTO       : N/A
 * Related Mapper    : PaymentFileMapper
 * Related DB Table  : payment_files
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentFileService, PaymentFileServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'payment_files' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.PaymentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentFileRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'payment_files' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code payment_files}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PaymentFileRepository extends JpaRepository<PaymentFile, Long> {
    Optional<PaymentFile> findByBatchId(Long batchId);
}