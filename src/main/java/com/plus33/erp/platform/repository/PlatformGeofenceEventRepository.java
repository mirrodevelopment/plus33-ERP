/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformGeofenceEventRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformGeofenceEventController
 * Related Service   : PlatformGeofenceEventService, PlatformGeofenceEventServiceImpl
 * Related Repository: PlatformGeofenceEventRepository
 * Related Entity    : PlatformGeofenceEvent
 * Related DTO       : N/A
 * Related Mapper    : PlatformGeofenceEventMapper
 * Related DB Table  : platform_geofence_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformGeofenceEventService, PlatformGeofenceEventServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_geofence_events' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformGeofenceEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformGeofenceEventRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_geofence_events' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_geofence_events}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformGeofenceEventRepository extends JpaRepository<PlatformGeofenceEvent, Long> {
}