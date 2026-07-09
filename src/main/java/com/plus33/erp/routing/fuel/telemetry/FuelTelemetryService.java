/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Routing Module
 * Package           : com.plus33.erp.routing.fuel.telemetry
 * File              : FuelTelemetryService.java
 * Purpose           : Business logic service layer for Routing Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FuelTelemetryController
 * Related Service   : FuelTelemetryService
 * Related Repository: FuelTelemetryRepository
 * Related Entity    : FuelTelemetry
 * Related DTO       : N/A
 * Related Mapper    : FuelTelemetryMapper
 * Related DB Table  : fuel_telemetrys
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : FuelTelemetryController, FuelTelemetryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Routing Module. Implements FuelTelemetryService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.routing.fuel.telemetry;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Routing Module</b>
 *
 * <p><b>Class  :</b> {@code FuelTelemetryService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.routing.fuel.telemetry}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Routing Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * FuelTelemetryController
 *   --> FuelTelemetryService (this)
 *   --> Validate business rules
 *   --> FuelTelemetryRepository (read/write 'fuel_telemetrys')
 *   --> FuelTelemetryMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code fuel_telemetrys}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class FuelTelemetryService {
    @Autowired PlatformFuelTelemetryLogRepository telemetryRepo;
    /**
     * Performs the recordTelemetry operation in this module.
     *
     * @param vehicleId the vehicleId input value
     * @param gatewayId the gatewayId input value
     * @return the PlatformFuelTelemetryLog result
     */
    @Transactional
    public PlatformFuelTelemetryLog recordTelemetry(Long vehicleId, Long gatewayId) {
        PlatformFuelTelemetryLog log = new PlatformFuelTelemetryLog();
        log.setVehicleId(vehicleId);
        log.setGatewayId(gatewayId);
        log.setEngineRpm(BigDecimal.valueOf(1450.00));
        log.setThrottlePositionPct(BigDecimal.valueOf(45.20));
        log.setInstantaneousFuelRate(BigDecimal.valueOf(22.40));
        log.setAverageFuelRate(BigDecimal.valueOf(24.50));
        log.setFuelLevelPct(BigDecimal.valueOf(88.00));
        log.setCoolantTemperatureC(BigDecimal.valueOf(85.00));
        log.setEngineLoadPct(BigDecimal.valueOf(62.00));
        log.setOdometerKm(BigDecimal.valueOf(12580.40));
        log.setTripDistanceKm(BigDecimal.valueOf(145.50));
        log.setIdleTimeSeconds(180);
        log.setGpsLatitude(BigDecimal.valueOf(37.774900));
        log.setGpsLongitude(BigDecimal.valueOf(-122.419400));
        log.setLoggedAt(LocalDateTime.now());
        return telemetryRepo.save(log);
    }
}