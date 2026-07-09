/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.repository
 * File              : TaxCalculationLogRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxCalculationLogController
 * Related Service   : TaxCalculationLogService, TaxCalculationLogServiceImpl
 * Related Repository: TaxCalculationLogRepository
 * Related Entity    : TaxCalculationLog
 * Related DTO       : N/A
 * Related Mapper    : TaxCalculationLogMapper
 * Related DB Table  : tax_calculation_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxCalculationLogService, TaxCalculationLogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'tax_calculation_logs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxCalculationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxCalculationLogRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'tax_calculation_logs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_calculation_logs}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TaxCalculationLogRepository extends JpaRepository<TaxCalculationLog, Long> {
    List<TaxCalculationLog> findByCompanyIdAndDocumentTypeAndDocumentId(Long companyId, String documentType, Long documentId);
}