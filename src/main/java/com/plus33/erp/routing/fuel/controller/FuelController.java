package com.plus33.erp.routing.fuel.controller;

import com.plus33.erp.routing.fuel.engine.FuelOptimizationEngine;
import com.plus33.erp.routing.fuel.diagnostic.EcoDrivingDiagnosticService;
import com.plus33.erp.routing.fuel.telemetry.FuelTelemetryService;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fuel")
public class FuelController {
    @Autowired FuelOptimizationEngine fuelEngine;
    @Autowired EcoDrivingDiagnosticService diagnosticService;
    @Autowired FuelTelemetryService telemetryService;

    @PostMapping("/policies")
    public ResponseEntity<Void> createPolicy(
            @RequestParam String code,
            @RequestParam String strategy) {
        fuelEngine.createPolicy(code, strategy);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/advisor")
    public ResponseEntity<Void> suggestAdvice(
            @RequestParam String type) {
        fuelEngine.suggestAdvice(type);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/telemetry")
    public ResponseEntity<Void> recordTelemetry(
            @RequestParam Long vehicleId,
            @RequestParam Long gatewayId) {
        telemetryService.recordTelemetry(vehicleId, gatewayId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/diagnostic")
    public ResponseEntity<Void> logEcoDriving(
            @RequestParam Long driverId,
            @RequestParam Long tripId) {
        diagnosticService.logEcoDrivingMetrics(driverId, tripId);
        return ResponseEntity.ok().build();
    }
}