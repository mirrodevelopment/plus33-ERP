/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : PaymentBatchRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentBatchController
 * Related Service   : PaymentBatchService, PaymentBatchServiceImpl
 * Related Repository: PaymentBatchRepository
 * Related Entity    : PaymentBatch
 * Related DTO       : N/A
 * Related Mapper    : PaymentBatchMapper
 * Related DB Table  : payment_batchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentBatchService, PaymentBatchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'payment_batchs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.PaymentBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentBatchRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'payment_batchs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code payment_batchs}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PaymentBatchRepository extends JpaRepository<PaymentBatch, Long> {
    List<PaymentBatch> findByCompanyId(Long companyId);
    Optional<PaymentBatch> findByCompanyIdAndBatchNumber(Long companyId, String batchNumber);
}