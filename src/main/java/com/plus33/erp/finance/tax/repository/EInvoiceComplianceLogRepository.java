/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.repository
 * File              : EInvoiceComplianceLogRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EInvoiceComplianceLogController
 * Related Service   : EInvoiceComplianceLogService, EInvoiceComplianceLogServiceImpl
 * Related Repository: EInvoiceComplianceLogRepository
 * Related Entity    : EInvoiceComplianceLog
 * Related DTO       : N/A
 * Related Mapper    : EInvoiceComplianceLogMapper
 * Related DB Table  : e_invoice_compliance_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EInvoiceComplianceLogService, EInvoiceComplianceLogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'e_invoice_compliance_logs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.EInvoiceComplianceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code EInvoiceComplianceLogRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'e_invoice_compliance_logs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code e_invoice_compliance_logs}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface EInvoiceComplianceLogRepository extends JpaRepository<EInvoiceComplianceLog, Long> {
    List<EInvoiceComplianceLog> findByCompanyIdAndDocumentTypeAndDocumentId(Long companyId, String documentType, Long documentId);
}