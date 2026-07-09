/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformEvBatteryHealthLogRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEvBatteryHealthLogController
 * Related Service   : PlatformEvBatteryHealthLogService, PlatformEvBatteryHealthLogServiceImpl
 * Related Repository: PlatformEvBatteryHealthLogRepository
 * Related Entity    : PlatformEvBatteryHealthLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformEvBatteryHealthLogMapper
 * Related DB Table  : platform_ev_battery_health_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEvBatteryHealthLogService, PlatformEvBatteryHealthLogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_ev_battery_health_logs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformEvBatteryHealthLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformEvBatteryHealthLogRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_ev_battery_health_logs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_ev_battery_health_logs}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformEvBatteryHealthLogRepository extends JpaRepository<PlatformEvBatteryHealthLog, Long> {
}