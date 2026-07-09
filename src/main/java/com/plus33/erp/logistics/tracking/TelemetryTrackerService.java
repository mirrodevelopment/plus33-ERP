/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Logistics Module
 * Package           : com.plus33.erp.logistics.tracking
 * File              : TelemetryTrackerService.java
 * Purpose           : Business logic service layer for Logistics Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TelemetryTrackerController
 * Related Service   : TelemetryTrackerService
 * Related Repository: TelemetryTrackerRepository
 * Related Entity    : TelemetryTracker
 * Related DTO       : N/A
 * Related Mapper    : TelemetryTrackerMapper
 * Related DB Table  : telemetry_trackers
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : TelemetryTrackerController, TelemetryTrackerServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Logistics Module. Implements TelemetryTrackerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.logistics.tracking;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Logistics Module</b>
 *
 * <p><b>Class  :</b> {@code TelemetryTrackerService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.logistics.tracking}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Logistics Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * TelemetryTrackerController
 *   --> TelemetryTrackerService (this)
 *   --> Validate business rules
 *   --> TelemetryTrackerRepository (read/write 'telemetry_trackers')
 *   --> TelemetryTrackerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code telemetry_trackers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class TelemetryTrackerService {
    @Autowired PlatformVehicleTelemetryRepository telemetryRepo;
    /**
     * Performs the track operation in this module.
     *
     * @param vehicleId the vehicleId input value
     * @param lat the lat input value
     * @param lon the lon input value
     * @param speed the speed input value
     * @return the PlatformVehicleTelemetry result
     */
    @Transactional
    public PlatformVehicleTelemetry track(Long vehicleId, BigDecimal lat, BigDecimal lon, BigDecimal speed) {
        PlatformVehicleTelemetry t = new PlatformVehicleTelemetry();
        t.setVehicleId(vehicleId);
        t.setLatitude(lat);
        t.setLongitude(lon);
        t.setSpeedKmh(speed);
        t.setRecordedAt(LocalDateTime.now());
        return telemetryRepo.save(t);
    }
}