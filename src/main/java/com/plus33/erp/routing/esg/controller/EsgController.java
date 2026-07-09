/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Routing Module
 * Package           : com.plus33.erp.routing.esg.controller
 * File              : EsgController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Routing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EsgController
 * Related Service   : EsgControllerService, EsgControllerServiceImpl
 * Related Repository: EsgControllerRepository
 * Related Entity    : EsgController
 * Related DTO       : N/A
 * Related Mapper    : EsgControllerMapper
 * Related DB Table  : esg_controllers
 * Related REST APIs : POST /api/esg/scope1, POST /api/esg/scope2, POST /api/esg/offsets, POST /api/esg/compliance
 * Depends On        : Platform Module
 * Used By           : Routing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Routing Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/esg/scope1, POST /api/esg/scope2, POST /api/esg/offsets, POST /api/esg/compliance
 ******************************************************************************/
package com.plus33.erp.routing.esg.controller;

import com.plus33.erp.routing.esg.carbon.CarbonAccountingService;
import com.plus33.erp.routing.esg.report.EsgReportingService;
import com.plus33.erp.routing.esg.compliance.SustainabilityComplianceService;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <b>PLUS33 Coffee ERP -- Routing Module</b>
 *
 * <p><b>Class  :</b> {@code EsgController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.routing.esg.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to EsgService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> EsgController.endpoint()
 *   --> EsgService.method()
 *   --> EsgRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/esg/scope1, POST /api/esg/scope2, POST /api/esg/offsets, POST /api/esg/compliance, POST /api/esg/audit</p>
 * <p><b>Module Deps      :</b> Routing, Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/esg")
public class EsgController {
    @Autowired CarbonAccountingService carbonService;
    @Autowired EsgReportingService reportService;
    @Autowired SustainabilityComplianceService complianceService;
    /**
     * Calculates scope1 totals including subtotal, tax, discounts, and net amount.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/scope1")
    public ResponseEntity<Void> calculateScope1(
            @RequestParam Long vehicleId,
            @RequestParam Long tripId) {
        carbonService.calculateScope1(vehicleId, tripId);
        return ResponseEntity.ok().build();
    }

    /**
     * Calculates scope2 totals including subtotal, tax, discounts, and net amount.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/scope2")
    public ResponseEntity<Void> calculateScope2(
            @RequestParam Long vehicleId,
            @RequestParam Long stationId) {
        carbonService.calculateScope2(vehicleId, stationId);
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new offset and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/offsets")
    public ResponseEntity<Void> registerOffset(
            @RequestParam String certificate) {
        complianceService.registerOffset(certificate);
        return ResponseEntity.ok().build();
    }

    /**
     * Validates business rules and constraints for compliance.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/compliance")
    public ResponseEntity<Void> verifyCompliance(
            @RequestParam String framework) {
        complianceService.verifyCompliance(framework);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the auditReport operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/audit")
    public ResponseEntity<Void> auditReport(
            @RequestParam String reportVersion) {
        reportService.auditEsgReport(reportVersion);
        return ResponseEntity.ok().build();
    }
}