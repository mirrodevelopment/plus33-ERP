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

@RestController
@RequestMapping("/api/logistics")
public class LogisticsController {
    @Autowired LogisticsNetworkService networkService;
    @Autowired TelemetryTrackerService trackingService;
    @Autowired GeoRoutingEngine routingEngine;
    @Autowired AutonomousReroutingCoordinator reroutingCoordinator;
    @Autowired DelayPredictionService predictionService;
    @Autowired FleetManager fleetManager;

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

    @PostMapping("/vehicles")
    public ResponseEntity<Void> registerVehicle(
            @RequestParam String code,
            @RequestParam BigDecimal capacity) {
        fleetManager.registerVehicle(code, capacity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tracking")
    public ResponseEntity<Void> trackVehicle(
            @RequestParam Long vehicleId,
            @RequestParam BigDecimal lat,
            @RequestParam BigDecimal lon,
            @RequestParam BigDecimal speed) {
        trackingService.track(vehicleId, lat, lon, speed);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/routes")
    public ResponseEntity<Void> planRoute(
            @RequestParam Long vehicleId,
            @RequestParam Long origin,
            @RequestParam Long dest,
            @RequestParam String path) {
        routingEngine.planRoute(vehicleId, origin, dest, path);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delays")
    public ResponseEntity<Void> predictDelay(
            @RequestParam Long routeId,
            @RequestParam BigDecimal confidence) {
        predictionService.predict(routeId, confidence);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rerouting")
    public ResponseEntity<Void> executeReroute(
            @RequestParam Long routeId,
            @RequestParam Long policyId,
            @RequestParam String newPath) {
        reroutingCoordinator.executeReroute(routeId, policyId, newPath);
        return ResponseEntity.ok().build();
    }
}