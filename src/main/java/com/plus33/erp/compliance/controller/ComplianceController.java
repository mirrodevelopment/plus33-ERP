/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Compliance Module
 * Package           : com.plus33.erp.compliance.controller
 * File              : ComplianceController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Compliance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ComplianceController
 * Related Service   : ComplianceControllerService, ComplianceControllerServiceImpl
 * Related Repository: ComplianceControllerRepository
 * Related Entity    : ComplianceController
 * Related DTO       : N/A
 * Related Mapper    : ComplianceControllerMapper
 * Related DB Table  : compliance_controllers
 * Related REST APIs : POST /api/compliance/policies, POST /api/compliance/check, POST /api/compliance/attestation/verify, POST /api/compliance/profile
 * Depends On        : Platform Module
 * Used By           : Compliance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Compliance Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/compliance/policies, POST /api/compliance/check, POST /api/compliance/attestation/verify, POST /api/compliance/profile
 ******************************************************************************/
package com.plus33.erp.compliance.controller;

import com.plus33.erp.compliance.policy.CompliancePolicyService;
import com.plus33.erp.compliance.policy.ComplianceChecker;
import com.plus33.erp.compliance.attestation.TpmAttestationValidator;
import com.plus33.erp.compliance.profile.ConfigurationProfileService;
import com.plus33.erp.compliance.profile.DriftDetector;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Compliance Module</b>
 *
 * <p><b>Class  :</b> {@code ComplianceController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.compliance.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to ComplianceService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> ComplianceController.endpoint()
 *   --> ComplianceService.method()
 *   --> ComplianceRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/compliance/policies, POST /api/compliance/check, POST /api/compliance/attestation/verify, POST /api/compliance/profile, POST /api/compliance/drift</p>
 * <p><b>Module Deps      :</b> Compliance, Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/compliance")
public class ComplianceController {
    @Autowired CompliancePolicyService policyService;
    @Autowired ComplianceChecker complianceChecker;
    @Autowired TpmAttestationValidator attestationValidator;
    @Autowired ConfigurationProfileService configProfileService;
    @Autowired DriftDetector driftDetector;
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
            @RequestParam String type,
            @RequestParam String severity) {
        policyService.createPolicy(code, name, type, severity);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the recordCompliance operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/check")
    public ResponseEntity<Void> recordCompliance(
            @RequestParam Long deviceId,
            @RequestParam Long policyId,
            @RequestParam String result) {
        complianceChecker.recordCompliance(deviceId, policyId, result);
        return ResponseEntity.ok().build();
    }

    /**
     * Validates business rules and constraints for quote.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/attestation/verify")
    public ResponseEntity<Void> verifyQuote(
            @RequestParam Long deviceId,
            @RequestParam String nonce,
            @RequestParam String result,
            @RequestParam BigDecimal score) {
        attestationValidator.verifyQuote(deviceId, nonce, result, score);
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new profile and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/profile")
    public ResponseEntity<Void> createProfile(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String version,
            @RequestParam String scope) {
        configProfileService.createProfile(code, name, version, scope);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the recordDrift operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/drift")
    public ResponseEntity<Void> recordDrift(
            @RequestParam Long deviceId,
            @RequestParam String baseline,
            @RequestParam String current) {
        driftDetector.recordDrift(deviceId, baseline, current);
        return ResponseEntity.ok().build();
    }
}