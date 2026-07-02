package com.plus33.erp.predictive.controller;

import com.plus33.erp.predictive.policy.PredictiveMaintenancePolicyService;
import com.plus33.erp.predictive.prognostics.FailurePrognosticsEngine;
import com.plus33.erp.predictive.reliability.ReliabilityEngineeringService;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/predictive")
public class PredictiveController {
    @Autowired PredictiveMaintenancePolicyService policyService;
    @Autowired FailurePrognosticsEngine prognosticsEngine;
    @Autowired ReliabilityEngineeringService reliabilityService;

    @PostMapping("/policies")
    public ResponseEntity<Void> createPolicy(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String assetType,
            @RequestParam String strategy) {
        policyService.createPolicy(code, name, assetType, strategy);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/prognostics/predict")
    public ResponseEntity<Void> predictFailure(
            @RequestParam Long assetId,
            @RequestParam BigDecimal rul,
            @RequestParam BigDecimal probability,
            @RequestParam BigDecimal confidence) {
        prognosticsEngine.predictFailure(assetId, rul, probability, confidence);
        return ResponseEntity.ok().build();
    }

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