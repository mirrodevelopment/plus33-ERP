/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.repository
 * File              : SupplierInvoiceRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierInvoiceController
 * Related Service   : SupplierInvoiceService, SupplierInvoiceServiceImpl
 * Related Repository: SupplierInvoiceRepository
 * Related Entity    : SupplierInvoice
 * Related DTO       : N/A
 * Related Mapper    : SupplierInvoiceMapper
 * Related DB Table  : supplier_invoices
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierInvoiceService, SupplierInvoiceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'supplier_invoices' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.repository;

import com.plus33.erp.finance.entity.SupplierInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierInvoiceRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'supplier_invoices' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code supplier_invoices}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface SupplierInvoiceRepository extends JpaRepository<SupplierInvoice, Long>, JpaSpecificationExecutor<SupplierInvoice> {
    Optional<SupplierInvoice> findBySupplierIdAndInvoiceNumber(Long supplierId, String invoiceNumber);
    List<SupplierInvoice> findByCompanyId(Long companyId);
    List<SupplierInvoice> findByPaymentRunId(Long paymentRunId);

    @Query("SELECT si FROM SupplierInvoice si WHERE si.company.id = :companyId " +
           "AND si.currencyCode = :currencyCode " +
           "AND si.status IN (com.plus33.erp.finance.entity.SupplierInvoiceStatus.APPROVED, com.plus33.erp.finance.entity.SupplierInvoiceStatus.PARTIALLY_PAID) " +
           "AND si.paymentRun IS NULL " +
           "AND (:supplierId IS NULL OR si.supplier.id = :supplierId) " +
           "AND (:dueDate IS NULL OR si.dueDate <= :dueDate)")
    List<SupplierInvoice> findEligibleForPaymentRun(
            @Param("companyId") Long companyId,
            @Param("currencyCode") String currencyCode,
            @Param("supplierId") Long supplierId,
            @Param("dueDate") LocalDate dueDate);

    @org.springframework.data.jpa.repository.Modifying
    @Query("UPDATE SupplierInvoice si SET si.paymentRun = null WHERE si.paymentRun.id = :runId")
    void releaseInvoicesForPaymentRun(@Param("runId") Long runId);
}

