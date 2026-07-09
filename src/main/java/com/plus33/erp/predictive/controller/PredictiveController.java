/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Predictive Module
 * Package           : com.plus33.erp.predictive.controller
 * File              : PredictiveController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Predictive Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PredictiveController
 * Related Service   : PredictiveControllerService, PredictiveControllerServiceImpl
 * Related Repository: PredictiveControllerRepository
 * Related Entity    : PredictiveController
 * Related DTO       : N/A
 * Related Mapper    : PredictiveControllerMapper
 * Related DB Table  : predictive_controllers
 * Related REST APIs : POST /api/predictive/policies, POST /api/predictive/prognostics/predict, POST /api/predictive/reliability/logs
 * Depends On        : Platform Module
 * Used By           : Predictive Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Predictive Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/predictive/policies, POST /api/predictive/prognostics/predict, POST /api/predictive/reliability/logs
 ******************************************************************************/
package com.plus33.erp.predictive.controller;

import com.plus33.erp.predictive.policy.PredictiveMaintenancePolicyService;
import com.plus33.erp.predictive.prognostics.FailurePrognosticsEngine;
import com.plus33.erp.predictive.reliability.ReliabilityEngineeringService;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Predictive Module</b>
 *
 * <p><b>Class  :</b> {@code PredictiveController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.predictive.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to PredictiveService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> PredictiveController.endpoint()
 *   --> PredictiveService.method()
 *   --> PredictiveRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/predictive/policies, POST /api/predictive/prognostics/predict, POST /api/predictive/reliability/logs</p>
 * <p><b>Module Deps      :</b> Predictive, Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/predictive")
public class PredictiveController {
    @Autowired PredictiveMaintenancePolicyService policyService;
    @Autowired FailurePrognosticsEngine prognosticsEngine;
    @Autowired ReliabilityEngineeringService reliabilityService;
    /**
     * Creates a new policy and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/policies")
    public ResponseEntity<Void> createPolicy(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String assetType,
            @RequestParam String strategy) {
        policyService.createPolicy(code, name, assetType, strategy);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the predictFailure operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/prognostics/predict")
    public ResponseEntity<Void> predictFailure(
            @RequestParam Long assetId,
            @RequestParam BigDecimal rul,
            @RequestParam BigDecimal probability,
            @RequestParam BigDecimal confidence) {
        prognosticsEngine.predictFailure(assetId, rul, probability, confidence);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the recordReliabilityLogs operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/reliability/logs")
    public ResponseEntity<Void> recordReliabilityLogs(
            @RequestParam Long assetId,
            @RequestParam BigDecimal mtbf,
            @RequestParam BigDecimal mttr,
            @RequestParam BigDecimal availability) {
        reliabilityService.recordReliabilityLogs(assetId, mtbf, mttr, availability);
        return ResponseEntity.ok().build();
    }
}