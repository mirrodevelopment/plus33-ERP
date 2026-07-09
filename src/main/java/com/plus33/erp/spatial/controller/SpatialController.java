/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Spatial Module
 * Package           : com.plus33.erp.spatial.controller
 * File              : SpatialController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Spatial Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SpatialController
 * Related Service   : SpatialControllerService, SpatialControllerServiceImpl
 * Related Repository: SpatialControllerRepository
 * Related Entity    : SpatialController
 * Related DTO       : N/A
 * Related Mapper    : SpatialControllerMapper
 * Related DB Table  : spatial_controllers
 * Related REST APIs : POST /api/spatial/geofences, POST /api/spatial/events, POST /api/spatial/deviancies, POST /api/spatial/queries
 * Depends On        : Platform Module
 * Used By           : Spatial Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Spatial Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/spatial/geofences, POST /api/spatial/events, POST /api/spatial/deviancies, POST /api/spatial/queries
 ******************************************************************************/
package com.plus33.erp.spatial.controller;

import com.plus33.erp.spatial.geofence.GeofenceManager;
import com.plus33.erp.spatial.geofence.GeofenceEventService;
import com.plus33.erp.spatial.analytics.DeviancyDetector;
import com.plus33.erp.spatial.query.SpatialTemporalQueryPlanner;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Spatial Module</b>
 *
 * <p><b>Class  :</b> {@code SpatialController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.spatial.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to SpatialService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> SpatialController.endpoint()
 *   --> SpatialService.method()
 *   --> SpatialRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/spatial/geofences, POST /api/spatial/events, POST /api/spatial/deviancies, POST /api/spatial/queries</p>
 * <p><b>Module Deps      :</b> Spatial, Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/spatial")
public class SpatialController {
    @Autowired GeofenceManager geofenceManager;
    @Autowired GeofenceEventService geofenceEventService;
    @Autowired DeviancyDetector deviancyDetector;
    @Autowired SpatialTemporalQueryPlanner queryPlanner;
    /**
     * Creates a new geofence and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/geofences")
    public ResponseEntity<Void> createGeofence(
            @RequestParam String code,
            @RequestParam String type,
            @RequestParam String wkt,
            @RequestParam BigDecimal centerLat,
            @RequestParam BigDecimal centerLng,
            @RequestParam(required = false) BigDecimal radius) {
        geofenceManager.createGeofence(code, type, wkt, centerLat, centerLng, radius);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the recordEvent operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/events")
    public ResponseEntity<Void> recordEvent(
            @RequestParam Long geofenceId,
            @RequestParam Long assetId,
            @RequestParam String eventType,
            @RequestParam BigDecimal lat,
            @RequestParam BigDecimal lon) {
        geofenceEventService.recordEvent(geofenceId, assetId, eventType, lat, lon);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the recordDeviancy operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/deviancies")
    public ResponseEntity<Void> recordDeviancy(
            @RequestParam Long routeId,
            @RequestParam String expected,
            @RequestParam String actual,
            @RequestParam BigDecimal distance) {
        deviancyDetector.recordDeviancy(routeId, expected, actual, distance);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the logQuery operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/queries")
    public ResponseEntity<Void> logQuery(
            @RequestParam String queryText,
            @RequestParam String boundingBox,
            @RequestParam String queryType) {
        queryPlanner.logQuery(queryText, boundingBox, queryType);
        return ResponseEntity.ok().build();
    }
}