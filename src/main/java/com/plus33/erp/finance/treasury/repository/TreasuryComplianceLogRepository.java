/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : TreasuryComplianceLogRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryComplianceLogController
 * Related Service   : TreasuryComplianceLogService, TreasuryComplianceLogServiceImpl
 * Related Repository: TreasuryComplianceLogRepository
 * Related Entity    : TreasuryComplianceLog
 * Related DTO       : N/A
 * Related Mapper    : TreasuryComplianceLogMapper
 * Related DB Table  : treasury_compliance_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TreasuryComplianceLogService, TreasuryComplianceLogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'treasury_compliance_logs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.TreasuryComplianceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryComplianceLogRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'treasury_compliance_logs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_compliance_logs}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TreasuryComplianceLogRepository extends JpaRepository<TreasuryComplianceLog, Long> {
    List<TreasuryComplianceLog> findByCompanyId(Long companyId);
}