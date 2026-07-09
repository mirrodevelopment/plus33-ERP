/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.repository
 * File              : CustomerInvoiceRepository.java
 * Purpose           : JPA Repository providing database CRUD for Sales Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerInvoiceController
 * Related Service   : CustomerInvoiceService, CustomerInvoiceServiceImpl
 * Related Repository: CustomerInvoiceRepository
 * Related Entity    : CustomerInvoice
 * Related DTO       : N/A
 * Related Mapper    : CustomerInvoiceMapper
 * Related DB Table  : customer_invoices
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CustomerInvoiceService, CustomerInvoiceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Sales Module against the 'customer_invoices' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.CustomerInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerInvoiceRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'customer_invoices' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code customer_invoices}</p>
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface CustomerInvoiceRepository extends JpaRepository<CustomerInvoice, Long>, JpaSpecificationExecutor<CustomerInvoice> {

    Optional<CustomerInvoice> findByCompanyIdAndInvoiceNumber(Long companyId, String invoiceNumber);

    Optional<CustomerInvoice> findByCompanyIdAndClientReferenceId(Long companyId, UUID clientReferenceId);

    @Query(value = "SELECT nextval('customer_invoice_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}