/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformAuditLogRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAuditLogController
 * Related Service   : PlatformAuditLogService, PlatformAuditLogServiceImpl
 * Related Repository: PlatformAuditLogRepository
 * Related Entity    : PlatformAuditLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformAuditLogMapper
 * Related DB Table  : platform_audit_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAuditLogService, PlatformAuditLogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_audit_logs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformAuditLogRepository extends JpaRepository<PlatformAuditLog, Long> {
}