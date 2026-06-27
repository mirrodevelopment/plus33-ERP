package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.EInvoiceComplianceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EInvoiceComplianceLogRepository extends JpaRepository<EInvoiceComplianceLog, Long> {
    List<EInvoiceComplianceLog> findByCompanyIdAndDocumentTypeAndDocumentId(Long companyId, String documentType, Long documentId);
}
