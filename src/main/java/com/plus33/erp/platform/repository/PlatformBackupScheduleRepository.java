/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformBackupScheduleRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformBackupScheduleController
 * Related Service   : PlatformBackupScheduleService, PlatformBackupScheduleServiceImpl
 * Related Repository: PlatformBackupScheduleRepository
 * Related Entity    : PlatformBackupSchedule
 * Related DTO       : N/A
 * Related Mapper    : PlatformBackupScheduleMapper
 * Related DB Table  : platform_backup_schedules
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformBackupScheduleService, PlatformBackupScheduleServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_backup_schedules' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformBackupSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformBackupScheduleRepository extends JpaRepository<PlatformBackupSchedule, Long> {
}