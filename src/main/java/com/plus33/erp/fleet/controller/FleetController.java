package com.plus33.erp.fleet.controller;

import com.plus33.erp.fleet.ota.OtaPackageManager;
import com.plus33.erp.fleet.ota.CampaignCoordinator;
import com.plus33.erp.fleet.diagnostic.DiagnosticCollector;
import com.plus33.erp.fleet.diagnostic.CrashReporter;
import com.plus33.erp.fleet.control.RemoteCommandService;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/fleet")
public class FleetController {
    @Autowired OtaPackageManager otaPackageManager;
    @Autowired CampaignCoordinator campaignCoordinator;
    @Autowired DiagnosticCollector diagnosticCollector;
    @Autowired CrashReporter crashReporter;
    @Autowired RemoteCommandService remoteCommandService;

    @PostMapping("/packages")
    public ResponseEntity<Void> createPackage(
            @RequestParam String name,
            @RequestParam String version,
            @RequestParam String checksum,
            @RequestParam String signature) {
        otaPackageManager.createPackage(name, version, checksum, signature);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/campaigns")
    public ResponseEntity<Void> createCampaign(
            @RequestParam String name,
            @RequestParam Long packageId,
            @RequestParam Long nodeId) {
        campaignCoordinator.createCampaign(name, packageId, nodeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/diagnostics/collect")
    public ResponseEntity<Void> collectDiagnostic(
            @RequestParam Long nodeId,
            @RequestParam BigDecimal cpu,
            @RequestParam BigDecimal memory) {
        diagnosticCollector.collectDiagnostic(nodeId, cpu, memory);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/diagnostics/crash")
    public ResponseEntity<Void> reportCrash(
            @RequestParam Long nodeId,
            @RequestParam String exception,
            @RequestParam String stackTrace) {
        crashReporter.reportCrash(nodeId, exception, stackTrace);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/commands/dispatch")
    public ResponseEntity<Void> dispatchCommand(
            @RequestParam Long nodeId,
            @RequestParam String commandType,
            @RequestParam String signature) {
        remoteCommandService.dispatchCommand(nodeId, commandType, signature);
        return ResponseEntity.ok().build();
    }
}