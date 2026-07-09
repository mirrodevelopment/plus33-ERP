/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.repository
 * File              : PaymentAllocationRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentAllocationController
 * Related Service   : PaymentAllocationService, PaymentAllocationServiceImpl
 * Related Repository: PaymentAllocationRepository
 * Related Entity    : PaymentAllocation
 * Related DTO       : N/A
 * Related Mapper    : PaymentAllocationMapper
 * Related DB Table  : payment_allocations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentAllocationService, PaymentAllocationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'payment_allocations' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.repository;

import com.plus33.erp.finance.entity.PaymentAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentAllocationRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'payment_allocations' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code payment_allocations}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PaymentAllocationRepository extends JpaRepository<PaymentAllocation, Long> {
    List<PaymentAllocation> findByPaymentId(Long paymentId);
    List<PaymentAllocation> findBySupplierInvoiceId(Long supplierInvoiceId);
    List<PaymentAllocation> findByCustomerInvoiceId(Long customerInvoiceId);
}
