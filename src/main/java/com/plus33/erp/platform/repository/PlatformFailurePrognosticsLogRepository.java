/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformFailurePrognosticsLogRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformFailurePrognosticsLogController
 * Related Service   : PlatformFailurePrognosticsLogService, PlatformFailurePrognosticsLogServiceImpl
 * Related Repository: PlatformFailurePrognosticsLogRepository
 * Related Entity    : PlatformFailurePrognosticsLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformFailurePrognosticsLogMapper
 * Related DB Table  : platform_failure_prognostics_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformFailurePrognosticsLogService, PlatformFailurePrognosticsLogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_failure_prognostics_logs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformFailurePrognosticsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformFailurePrognosticsLogRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_failure_prognostics_logs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_failure_prognostics_logs}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformFailurePrognosticsLogRepository extends JpaRepository<PlatformFailurePrognosticsLog, Long> {
}