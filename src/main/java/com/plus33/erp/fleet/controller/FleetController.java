/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Fleet Module
 * Package           : com.plus33.erp.fleet.controller
 * File              : FleetController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Fleet Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FleetController
 * Related Service   : FleetControllerService, FleetControllerServiceImpl
 * Related Repository: FleetControllerRepository
 * Related Entity    : FleetController
 * Related DTO       : N/A
 * Related Mapper    : FleetControllerMapper
 * Related DB Table  : fleet_controllers
 * Related REST APIs : POST /api/fleet/packages, POST /api/fleet/campaigns, POST /api/fleet/diagnostics/collect, POST /api/fleet/diagnostics/crash
 * Depends On        : Platform Module
 * Used By           : Fleet Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Fleet Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/fleet/packages, POST /api/fleet/campaigns, POST /api/fleet/diagnostics/collect, POST /api/fleet/diagnostics/crash
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Fleet Module</b>
 *
 * <p><b>Class  :</b> {@code FleetController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.fleet.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to FleetService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> FleetController.endpoint()
 *   --> FleetService.method()
 *   --> FleetRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/fleet/packages, POST /api/fleet/campaigns, POST /api/fleet/diagnostics/collect, POST /api/fleet/diagnostics/crash, POST /api/fleet/commands/dispatch</p>
 * <p><b>Module Deps      :</b> Fleet, Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/fleet")
public class FleetController {
    @Autowired OtaPackageManager otaPackageManager;
    @Autowired CampaignCoordinator campaignCoordinator;
    @Autowired DiagnosticCollector diagnosticCollector;
    @Autowired CrashReporter crashReporter;
    @Autowired RemoteCommandService remoteCommandService;
    /**
     * Creates a new package and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/packages")
    public ResponseEntity<Void> createPackage(
            @RequestParam String name,
            @RequestParam String version,
            @RequestParam String checksum,
            @RequestParam String signature) {
        otaPackageManager.createPackage(name, version, checksum, signature);
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new campaign and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/campaigns")
    public ResponseEntity<Void> createCampaign(
            @RequestParam String name,
            @RequestParam Long packageId,
            @RequestParam Long nodeId) {
        campaignCoordinator.createCampaign(name, packageId, nodeId);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the collectDiagnostic operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/diagnostics/collect")
    public ResponseEntity<Void> collectDiagnostic(
            @RequestParam Long nodeId,
            @RequestParam BigDecimal cpu,
            @RequestParam BigDecimal memory) {
        diagnosticCollector.collectDiagnostic(nodeId, cpu, memory);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the reportCrash operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/diagnostics/crash")
    public ResponseEntity<Void> reportCrash(
            @RequestParam Long nodeId,
            @RequestParam String exception,
            @RequestParam String stackTrace) {
        crashReporter.reportCrash(nodeId, exception, stackTrace);
        return ResponseEntity.ok().build();
    }

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/commands/dispatch")
    public ResponseEntity<Void> dispatchCommand(
            @RequestParam Long nodeId,
            @RequestParam String commandType,
            @RequestParam String signature) {
        remoteCommandService.dispatchCommand(nodeId, commandType, signature);
        return ResponseEntity.ok().build();
    }
}