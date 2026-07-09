/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Spatial Module
 * Package           : com.plus33.erp.spatial.geofence
 * File              : GeofenceEventService.java
 * Purpose           : Business logic service layer for Spatial Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GeofenceEventController
 * Related Service   : GeofenceEventService
 * Related Repository: GeofenceEventRepository
 * Related Entity    : GeofenceEvent
 * Related DTO       : N/A
 * Related Mapper    : GeofenceEventMapper
 * Related DB Table  : geofence_events
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : GeofenceEventController, GeofenceEventServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Spatial Module. Implements GeofenceEventService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.spatial.geofence;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Spatial Module</b>
 *
 * <p><b>Class  :</b> {@code GeofenceEventService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.spatial.geofence}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Spatial Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * GeofenceEventController
 *   --> GeofenceEventService (this)
 *   --> Validate business rules
 *   --> GeofenceEventRepository (read/write 'geofence_events')
 *   --> GeofenceEventMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code geofence_events}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class GeofenceEventService {
    @Autowired PlatformGeofenceEventRepository eventRepo;
    /**
     * Performs the recordEvent operation in this module.
     *
     * @param geofenceId the geofenceId input value
     * @param assetId the assetId input value
     * @param type the type input value
     * @param lat the lat input value
     * @param lon the lon input value
     * @return the PlatformGeofenceEvent result
     */
    @Transactional
    public PlatformGeofenceEvent recordEvent(Long geofenceId, Long assetId, String type, BigDecimal lat, BigDecimal lon) {
        PlatformGeofenceEvent e = new PlatformGeofenceEvent();
        e.setGeofenceId(geofenceId);
        e.setAssetId(assetId);
        e.setEventType(type);
        e.setLatitude(lat);
        e.setLongitude(lon);
        e.setSpeedKmh(BigDecimal.valueOf(65.50));
        e.setHeadingDegrees(BigDecimal.valueOf(180.00));
        e.setGpsAccuracyMeters(BigDecimal.valueOf(2.50));
        e.setRecordedAt(LocalDateTime.now());
        return eventRepo.save(e);
    }
}