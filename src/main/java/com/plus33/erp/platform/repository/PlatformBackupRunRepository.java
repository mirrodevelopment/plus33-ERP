/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformBackupRunRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformBackupRunController
 * Related Service   : PlatformBackupRunService, PlatformBackupRunServiceImpl
 * Related Repository: PlatformBackupRunRepository
 * Related Entity    : PlatformBackupRun
 * Related DTO       : N/A
 * Related Mapper    : PlatformBackupRunMapper
 * Related DB Table  : platform_backup_runs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformBackupRunService, PlatformBackupRunServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_backup_runs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformBackupRun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformBackupRunRepository extends JpaRepository<PlatformBackupRun, Long> {
}