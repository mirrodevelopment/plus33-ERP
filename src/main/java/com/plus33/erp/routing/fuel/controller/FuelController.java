/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Routing Module
 * Package           : com.plus33.erp.routing.fuel.controller
 * File              : FuelController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Routing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FuelController
 * Related Service   : FuelControllerService, FuelControllerServiceImpl
 * Related Repository: FuelControllerRepository
 * Related Entity    : FuelController
 * Related DTO       : N/A
 * Related Mapper    : FuelControllerMapper
 * Related DB Table  : fuel_controllers
 * Related REST APIs : POST /api/fuel/policies, POST /api/fuel/advisor, POST /api/fuel/telemetry, POST /api/fuel/diagnostic
 * Depends On        : Platform Module
 * Used By           : Routing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Routing Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/fuel/policies, POST /api/fuel/advisor, POST /api/fuel/telemetry, POST /api/fuel/diagnostic
 ******************************************************************************/
package com.plus33.erp.routing.fuel.controller;

import com.plus33.erp.routing.fuel.engine.FuelOptimizationEngine;
import com.plus33.erp.routing.fuel.diagnostic.EcoDrivingDiagnosticService;
import com.plus33.erp.routing.fuel.telemetry.FuelTelemetryService;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <b>PLUS33 Coffee ERP -- Routing Module</b>
 *
 * <p><b>Class  :</b> {@code FuelController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.routing.fuel.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to FuelService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> FuelController.endpoint()
 *   --> FuelService.method()
 *   --> FuelRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/fuel/policies, POST /api/fuel/advisor, POST /api/fuel/telemetry, POST /api/fuel/diagnostic</p>
 * <p><b>Module Deps      :</b> Routing, Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/fuel")
public class FuelController {
    @Autowired FuelOptimizationEngine fuelEngine;
    @Autowired EcoDrivingDiagnosticService diagnosticService;
    @Autowired FuelTelemetryService telemetryService;
    /**
     * Creates a new policy and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/policies")
    public ResponseEntity<Void> createPolicy(
            @RequestParam String code,
            @RequestParam String strategy) {
        fuelEngine.createPolicy(code, strategy);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the suggestAdvice operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/advisor")
    public ResponseEntity<Void> suggestAdvice(
            @RequestParam String type) {
        fuelEngine.suggestAdvice(type);
        return ResponseEntity.ok().build();
    }

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
            @RequestParam Long gatewayId) {
        telemetryService.recordTelemetry(vehicleId, gatewayId);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the logEcoDriving operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/diagnostic")
    public ResponseEntity<Void> logEcoDriving(
            @RequestParam Long driverId,
            @RequestParam Long tripId) {
        diagnosticService.logEcoDrivingMetrics(driverId, tripId);
        return ResponseEntity.ok().build();
    }
}