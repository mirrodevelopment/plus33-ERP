package com.plus33.erp.routing.ev.controller;

import com.plus33.erp.routing.ev.energy.EvEnergyManagementService;
import com.plus33.erp.routing.ev.diagnostic.BatteryHealthDiagnosticService;
import com.plus33.erp.routing.ev.scheduler.ChargingStationScheduler;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ev")
public class EvController {
    @Autowired EvEnergyManagementService energyService;
    @Autowired BatteryHealthDiagnosticService diagnosticService;
    @Autowired ChargingStationScheduler scheduler;

    @PostMapping("/telemetry")
    public ResponseEntity<Void> recordTelemetry(
            @RequestParam Long vehicleId,
            @RequestParam String batteryPackId) {
        energyService.recordTelemetry(vehicleId, batteryPackId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/diagnose")
    public ResponseEntity<Void> diagnoseBattery(
            @RequestParam Long vehicleId) {
        diagnosticService.diagnoseBattery(vehicleId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stations")
    public ResponseEntity<Void> registerStation(
            @RequestParam String code,
            @RequestParam String operator) {
        scheduler.registerStation(code, operator);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reserve")
    public ResponseEntity<Void> reserveSlot(
            @RequestParam Long vehicleId,
            @RequestParam Long stationId) {
        scheduler.reserveSlot(vehicleId, stationId);
        return ResponseEntity.ok().build();
    }
}