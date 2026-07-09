/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformPredictiveAuditLogRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformPredictiveAuditLogController
 * Related Service   : PlatformPredictiveAuditLogService, PlatformPredictiveAuditLogServiceImpl
 * Related Repository: PlatformPredictiveAuditLogRepository
 * Related Entity    : PlatformPredictiveAuditLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformPredictiveAuditLogMapper
 * Related DB Table  : platform_predictive_audit_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformPredictiveAuditLogService, PlatformPredictiveAuditLogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_predictive_audit_logs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformPredictiveAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformPredictiveAuditLogRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_predictive_audit_logs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_predictive_audit_logs}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformPredictiveAuditLogRepository extends JpaRepository<PlatformPredictiveAuditLog, Long> {
}