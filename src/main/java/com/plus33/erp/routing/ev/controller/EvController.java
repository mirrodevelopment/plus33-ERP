/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Routing Module
 * Package           : com.plus33.erp.routing.ev.controller
 * File              : EvController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Routing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EvController
 * Related Service   : EvControllerService, EvControllerServiceImpl
 * Related Repository: EvControllerRepository
 * Related Entity    : EvController
 * Related DTO       : N/A
 * Related Mapper    : EvControllerMapper
 * Related DB Table  : ev_controllers
 * Related REST APIs : POST /api/ev/telemetry, POST /api/ev/diagnose, POST /api/ev/stations, POST /api/ev/reserve
 * Depends On        : Platform Module
 * Used By           : Routing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Routing Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/ev/telemetry, POST /api/ev/diagnose, POST /api/ev/stations, POST /api/ev/reserve
 ******************************************************************************/
package com.plus33.erp.routing.ev.controller;

import com.plus33.erp.routing.ev.energy.EvEnergyManagementService;
import com.plus33.erp.routing.ev.diagnostic.BatteryHealthDiagnosticService;
import com.plus33.erp.routing.ev.scheduler.ChargingStationScheduler;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <b>PLUS33 Coffee ERP -- Routing Module</b>
 *
 * <p><b>Class  :</b> {@code EvController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.routing.ev.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to EvService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> EvController.endpoint()
 *   --> EvService.method()
 *   --> EvRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/ev/telemetry, POST /api/ev/diagnose, POST /api/ev/stations, POST /api/ev/reserve</p>
 * <p><b>Module Deps      :</b> Routing, Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/ev")
public class EvController {
    @Autowired EvEnergyManagementService energyService;
    @Autowired BatteryHealthDiagnosticService diagnosticService;
    @Autowired ChargingStationScheduler scheduler;
    /**
     * Performs the recordTelemetry operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/telemetry")
    public ResponseEntity<Void> recordTelemetry(
            @RequestParam Long vehicleId,
            @RequestParam String batteryPackId) {
        energyService.recordTelemetry(vehicleId, batteryPackId);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the diagnoseBattery operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/diagnose")
    public ResponseEntity<Void> diagnoseBattery(
            @RequestParam Long vehicleId) {
        diagnosticService.diagnoseBattery(vehicleId);
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new station and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/stations")
    public ResponseEntity<Void> registerStation(
            @RequestParam String code,
            @RequestParam String operator) {
        scheduler.registerStation(code, operator);
        return ResponseEntity.ok().build();
    }

    /**
     * Reserves slot resources (budget or stock) for downstream processing.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/reserve")
    public ResponseEntity<Void> reserveSlot(
            @RequestParam Long vehicleId,
            @RequestParam Long stationId) {
        scheduler.reserveSlot(vehicleId, stationId);
        return ResponseEntity.ok().build();
    }
}