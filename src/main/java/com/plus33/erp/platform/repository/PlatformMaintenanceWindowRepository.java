/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformMaintenanceWindowRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformMaintenanceWindowController
 * Related Service   : PlatformMaintenanceWindowService, PlatformMaintenanceWindowServiceImpl
 * Related Repository: PlatformMaintenanceWindowRepository
 * Related Entity    : PlatformMaintenanceWindow
 * Related DTO       : N/A
 * Related Mapper    : PlatformMaintenanceWindowMapper
 * Related DB Table  : platform_maintenance_windows
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformMaintenanceWindowService, PlatformMaintenanceWindowServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_maintenance_windows' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformMaintenanceWindow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformMaintenanceWindowRepository extends JpaRepository<PlatformMaintenanceWindow, Long> {
}