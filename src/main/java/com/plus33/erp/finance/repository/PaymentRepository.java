/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.repository
 * File              : PaymentRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentController
 * Related Service   : PaymentService, PaymentServiceImpl
 * Related Repository: PaymentRepository
 * Related Entity    : Payment
 * Related DTO       : N/A
 * Related Mapper    : PaymentMapper
 * Related DB Table  : payments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentService, PaymentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'payments' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.repository;

import com.plus33.erp.finance.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'payments' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code payments}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {
    Optional<Payment> findByCompanyIdAndPaymentNumber(Long companyId, String paymentNumber);
    List<Payment> findByCompanyId(Long companyId);
    Optional<Payment> findByCompanyIdAndReferenceNumber(Long companyId, String referenceNumber);

    @Query(value = "SELECT nextval('payment_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
