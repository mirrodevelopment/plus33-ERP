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

@RestController
@RequestMapping("/api/compliance")
public class ComplianceController {
    @Autowired CompliancePolicyService policyService;
    @Autowired ComplianceChecker complianceChecker;
    @Autowired TpmAttestationValidator attestationValidator;
    @Autowired ConfigurationProfileService configProfileService;
    @Autowired DriftDetector driftDetector;

    @PostMapping("/policies")
    public ResponseEntity<Void> createPolicy(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam String severity) {
        policyService.createPolicy(code, name, type, severity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check")
    public ResponseEntity<Void> recordCompliance(
            @RequestParam Long deviceId,
            @RequestParam Long policyId,
            @RequestParam String result) {
        complianceChecker.recordCompliance(deviceId, policyId, result);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/attestation/verify")
    public ResponseEntity<Void> verifyQuote(
            @RequestParam Long deviceId,
            @RequestParam String nonce,
            @RequestParam String result,
            @RequestParam BigDecimal score) {
        attestationValidator.verifyQuote(deviceId, nonce, result, score);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/profile")
    public ResponseEntity<Void> createProfile(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String version,
            @RequestParam String scope) {
        configProfileService.createProfile(code, name, version, scope);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/drift")
    public ResponseEntity<Void> recordDrift(
            @RequestParam Long deviceId,
            @RequestParam String baseline,
            @RequestParam String current) {
        driftDetector.recordDrift(deviceId, baseline, current);
        return ResponseEntity.ok().build();
    }
}