package com.plus33.erp.finance.repository;

import com.plus33.erp.finance.entity.SupplierInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

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

