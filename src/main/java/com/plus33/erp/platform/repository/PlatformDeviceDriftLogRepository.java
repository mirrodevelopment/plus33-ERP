/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformDeviceDriftLogRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDeviceDriftLogController
 * Related Service   : PlatformDeviceDriftLogService, PlatformDeviceDriftLogServiceImpl
 * Related Repository: PlatformDeviceDriftLogRepository
 * Related Entity    : PlatformDeviceDriftLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformDeviceDriftLogMapper
 * Related DB Table  : platform_device_drift_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDeviceDriftLogService, PlatformDeviceDriftLogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_device_drift_logs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformDeviceDriftLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformDeviceDriftLogRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_device_drift_logs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_device_drift_logs}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformDeviceDriftLogRepository extends JpaRepository<PlatformDeviceDriftLog, Long> {
}