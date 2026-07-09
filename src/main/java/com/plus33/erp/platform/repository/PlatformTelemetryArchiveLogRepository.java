/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformTelemetryArchiveLogRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTelemetryArchiveLogController
 * Related Service   : PlatformTelemetryArchiveLogService, PlatformTelemetryArchiveLogServiceImpl
 * Related Repository: PlatformTelemetryArchiveLogRepository
 * Related Entity    : PlatformTelemetryArchiveLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformTelemetryArchiveLogMapper
 * Related DB Table  : platform_telemetry_archive_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTelemetryArchiveLogService, PlatformTelemetryArchiveLogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_telemetry_archive_logs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformTelemetryArchiveLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformTelemetryArchiveLogRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_telemetry_archive_logs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_telemetry_archive_logs}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformTelemetryArchiveLogRepository extends JpaRepository<PlatformTelemetryArchiveLog, Long> {
}