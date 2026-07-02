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

@RestController
@RequestMapping("/api/spatial")
public class SpatialController {
    @Autowired GeofenceManager geofenceManager;
    @Autowired GeofenceEventService geofenceEventService;
    @Autowired DeviancyDetector deviancyDetector;
    @Autowired SpatialTemporalQueryPlanner queryPlanner;

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

    @PostMapping("/deviancies")
    public ResponseEntity<Void> recordDeviancy(
            @RequestParam Long routeId,
            @RequestParam String expected,
            @RequestParam String actual,
            @RequestParam BigDecimal distance) {
        deviancyDetector.recordDeviancy(routeId, expected, actual, distance);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/queries")
    public ResponseEntity<Void> logQuery(
            @RequestParam String queryText,
            @RequestParam String boundingBox,
            @RequestParam String queryType) {
        queryPlanner.logQuery(queryText, boundingBox, queryType);
        return ResponseEntity.ok().build();
    }
}