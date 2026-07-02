package com.plus33.erp.routing.esg.controller;

import com.plus33.erp.routing.esg.carbon.CarbonAccountingService;
import com.plus33.erp.routing.esg.report.EsgReportingService;
import com.plus33.erp.routing.esg.compliance.SustainabilityComplianceService;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/esg")
public class EsgController {
    @Autowired CarbonAccountingService carbonService;
    @Autowired EsgReportingService reportService;
    @Autowired SustainabilityComplianceService complianceService;

    @PostMapping("/scope1")
    public ResponseEntity<Void> calculateScope1(
            @RequestParam Long vehicleId,
            @RequestParam Long tripId) {
        carbonService.calculateScope1(vehicleId, tripId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/scope2")
    public ResponseEntity<Void> calculateScope2(
            @RequestParam Long vehicleId,
            @RequestParam Long stationId) {
        carbonService.calculateScope2(vehicleId, stationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/offsets")
    public ResponseEntity<Void> registerOffset(
            @RequestParam String certificate) {
        complianceService.registerOffset(certificate);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/compliance")
    public ResponseEntity<Void> verifyCompliance(
            @RequestParam String framework) {
        complianceService.verifyCompliance(framework);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/audit")
    public ResponseEntity<Void> auditReport(
            @RequestParam String reportVersion) {
        reportService.auditEsgReport(reportVersion);
        return ResponseEntity.ok().build();
    }
}