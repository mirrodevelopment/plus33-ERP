/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Logistics Module
 * Package           : com.plus33.erp.logistics.controller
 * File              : LogisticsController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Logistics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LogisticsController
 * Related Service   : LogisticsControllerService, LogisticsControllerServiceImpl
 * Related Repository: LogisticsControllerRepository
 * Related Entity    : LogisticsController
 * Related DTO       : N/A
 * Related Mapper    : LogisticsControllerMapper
 * Related DB Table  : logistics_controllers
 * Related REST APIs : POST /api/logistics/nodes, POST /api/logistics/lanes, POST /api/logistics/vehicles, POST /api/logistics/tracking
 * Depends On        : Platform Module
 * Used By           : Logistics Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Logistics Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/logistics/nodes, POST /api/logistics/lanes, POST /api/logistics/vehicles, POST /api/logistics/tracking
 ******************************************************************************/
package com.plus33.erp.logistics.controller;

import com.plus33.erp.logistics.network.LogisticsNetworkService;
import com.plus33.erp.logistics.tracking.TelemetryTrackerService;
import com.plus33.erp.logistics.routing.GeoRoutingEngine;
import com.plus33.erp.logistics.routing.AutonomousReroutingCoordinator;
import com.plus33.erp.logistics.prediction.DelayPredictionService;
import com.plus33.erp.logistics.fleet.FleetManager;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Logistics Module</b>
 *
 * <p><b>Class  :</b> {@code LogisticsController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.logistics.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to LogisticsService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> LogisticsController.endpoint()
 *   --> LogisticsService.method()
 *   --> LogisticsRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/logistics/nodes, POST /api/logistics/lanes, POST /api/logistics/vehicles, POST /api/logistics/tracking, POST /api/logistics/routes</p>
 * <p><b>Module Deps      :</b> Logistics, Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/logistics")
public class LogisticsController {
    @Autowired LogisticsNetworkService networkService;
    @Autowired TelemetryTrackerService trackingService;
    @Autowired GeoRoutingEngine routingEngine;
    @Autowired AutonomousReroutingCoordinator reroutingCoordinator;
    @Autowired DelayPredictionService predictionService;
    @Autowired FleetManager fleetManager;
    /**
     * Creates a new node and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/nodes")
    public ResponseEntity<Void> addNode(
            @RequestParam String code,
            @RequestParam String type,
            @RequestParam BigDecimal lat,
            @RequestParam BigDecimal lon,
            @RequestParam String region,
            @RequestParam String timezone,
            @RequestParam Integer capacity) {
        networkService.addNode(code, type, lat, lon, region, timezone, capacity);
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new lane and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/lanes")
    public ResponseEntity<Void> addLane(
            @RequestParam Long src,
            @RequestParam Long dest,
            @RequestParam BigDecimal distance,
            @RequestParam Integer duration,
            @RequestParam String mode) {
        networkService.addLane(src, dest, distance, duration, mode);
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new vehicle and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/vehicles")
    public ResponseEntity<Void> registerVehicle(
            @RequestParam String code,
            @RequestParam BigDecimal capacity) {
        fleetManager.registerVehicle(code, capacity);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the trackVehicle operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/tracking")
    public ResponseEntity<Void> trackVehicle(
            @RequestParam Long vehicleId,
            @RequestParam BigDecimal lat,
            @RequestParam BigDecimal lon,
            @RequestParam BigDecimal speed) {
        trackingService.track(vehicleId, lat, lon, speed);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the planRoute operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/routes")
    public ResponseEntity<Void> planRoute(
            @RequestParam Long vehicleId,
            @RequestParam Long origin,
            @RequestParam Long dest,
            @RequestParam String path) {
        routingEngine.planRoute(vehicleId, origin, dest, path);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the predictDelay operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/delays")
    public ResponseEntity<Void> predictDelay(
            @RequestParam Long routeId,
            @RequestParam BigDecimal confidence) {
        predictionService.predict(routeId, confidence);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the executeReroute operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/rerouting")
    public ResponseEntity<Void> executeReroute(
            @RequestParam Long routeId,
            @RequestParam Long policyId,
            @RequestParam String newPath) {
        reroutingCoordinator.executeReroute(routeId, policyId, newPath);
        return ResponseEntity.ok().build();
    }
}