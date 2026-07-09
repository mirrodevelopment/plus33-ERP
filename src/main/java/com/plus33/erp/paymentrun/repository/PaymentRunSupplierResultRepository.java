/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.repository
 * File              : PaymentRunSupplierResultRepository.java
 * Purpose           : JPA Repository providing database CRUD for Paymentrun Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentRunSupplierResultController
 * Related Service   : PaymentRunSupplierResultService, PaymentRunSupplierResultServiceImpl
 * Related Repository: PaymentRunSupplierResultRepository
 * Related Entity    : PaymentRunSupplierResult
 * Related DTO       : N/A
 * Related Mapper    : PaymentRunSupplierResultMapper
 * Related DB Table  : payment_run_supplier_results
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentRunSupplierResultService, PaymentRunSupplierResultServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Paymentrun Module against the 'payment_run_supplier_results' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.paymentrun.repository;

import com.plus33.erp.paymentrun.entity.PaymentRunSupplierResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Paymentrun Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentRunSupplierResultRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.paymentrun.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'payment_run_supplier_results' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code payment_run_supplier_results}</p>
 * <p><b>Module Deps      :</b> Paymentrun</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PaymentRunSupplierResultRepository extends JpaRepository<PaymentRunSupplierResult, Long> {
    List<PaymentRunSupplierResult> findByPaymentRunId(Long paymentRunId);
}
