package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxCalculationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxCalculationLogRepository extends JpaRepository<TaxCalculationLog, Long> {
    List<TaxCalculationLog> findByCompanyIdAndDocumentTypeAndDocumentId(Long companyId, String documentType, Long documentId);
}
