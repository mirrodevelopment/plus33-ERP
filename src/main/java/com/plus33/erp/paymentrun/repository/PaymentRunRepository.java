/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.repository
 * File              : PaymentRunRepository.java
 * Purpose           : JPA Repository providing database CRUD for Paymentrun Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentRunController
 * Related Service   : PaymentRunService, PaymentRunServiceImpl
 * Related Repository: PaymentRunRepository
 * Related Entity    : PaymentRun
 * Related DTO       : N/A
 * Related Mapper    : PaymentRunMapper
 * Related DB Table  : payment_runs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentRunService, PaymentRunServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Paymentrun Module against the 'payment_runs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.paymentrun.repository;

import com.plus33.erp.paymentrun.entity.PaymentRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Paymentrun Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentRunRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.paymentrun.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'payment_runs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code payment_runs}</p>
 * <p><b>Module Deps      :</b> Paymentrun</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PaymentRunRepository extends JpaRepository<PaymentRun, Long>, JpaSpecificationExecutor<PaymentRun> {
    Optional<PaymentRun> findByRunNumber(String runNumber);
    Optional<PaymentRun> findByClientReferenceId(UUID clientReferenceId);

    @Query(value = "SELECT nextval('payment_run_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}

