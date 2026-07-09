/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformRoutingAuditLogRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformRoutingAuditLogController
 * Related Service   : PlatformRoutingAuditLogService, PlatformRoutingAuditLogServiceImpl
 * Related Repository: PlatformRoutingAuditLogRepository
 * Related Entity    : PlatformRoutingAuditLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformRoutingAuditLogMapper
 * Related DB Table  : platform_routing_audit_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformRoutingAuditLogService, PlatformRoutingAuditLogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_routing_audit_logs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformRoutingAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformRoutingAuditLogRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_routing_audit_logs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_routing_audit_logs}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformRoutingAuditLogRepository extends JpaRepository<PlatformRoutingAuditLog, Long> {
}