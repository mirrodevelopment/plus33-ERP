package com.plus33.erp.finance.repository;

import com.plus33.erp.finance.entity.SupplierInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;
import java.util.List;

public interface SupplierInvoiceRepository extends JpaRepository<SupplierInvoice, Long>, JpaSpecificationExecutor<SupplierInvoice> {
    Optional<SupplierInvoice> findBySupplierIdAndInvoiceNumber(Long supplierId, String invoiceNumber);
    List<SupplierInvoice> findByCompanyId(Long companyId);
}
