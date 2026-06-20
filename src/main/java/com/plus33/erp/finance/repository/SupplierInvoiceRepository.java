package com.plus33.erp.finance.repository;

import com.plus33.erp.finance.entity.SupplierInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface SupplierInvoiceRepository extends JpaRepository<SupplierInvoice, Long> {
    Optional<SupplierInvoice> findBySupplierIdAndInvoiceNumber(Long supplierId, String invoiceNumber);
    List<SupplierInvoice> findByCompanyId(Long companyId);
}
