package com.plus33.erp.platform.controller;

import com.plus33.erp.platform.config.DistributedConfigService;
import com.plus33.erp.platform.featureflag.FeatureFlagService;
import com.plus33.erp.platform.metrics.PrometheusExporterService;
import com.plus33.erp.platform.audit.PlatformAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/platform")
public class PlatformOpsController {
    @Autowired DistributedConfigService configService;
    @Autowired FeatureFlagService flagService;
    @Autowired PrometheusExporterService metricsService;
    @Autowired PlatformAuditService auditService;

    @GetMapping("/config")
    public ResponseEntity<String> getConfig(@RequestParam String key, @RequestParam String profile) {
        String val = configService.getConfig(key, profile);
        return val != null ? ResponseEntity.ok(val) : ResponseEntity.notFound().build();
    }

    @PostMapping("/config")
    public ResponseEntity<Void> setConfig(
            @RequestParam String key, 
            @RequestParam String value, 
            @RequestParam String profile,
            @RequestParam String operator) {
        configService.setConfig(key, value, profile, operator);
        auditService.logAudit("SET_CONFIG", operator, "REST", "key=" + key + ", value=" + value);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/config/rollback")
    public ResponseEntity<Void> rollbackConfig(
            @RequestParam String key,
            @RequestParam String profile,
            @RequestParam int version,
            @RequestParam String operator) {
        configService.rollbackConfig(key, profile, version, operator);
        auditService.logAudit("ROLLBACK_CONFIG", operator, "REST", "key=" + key + ", version=" + version);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/flag")
    public ResponseEntity<Boolean> getFlag(@RequestParam String key, @RequestParam String userId) {
        return ResponseEntity.ok(flagService.isEnabled(key, userId));
    }

    @PostMapping("/flag")
    public ResponseEntity<Void> updateFlag(
            @RequestParam String key,
            @RequestParam String status,
            @RequestParam int rollout,
            @RequestParam String reason,
            @RequestParam String operator) {
        flagService.updateFlag(key, status, rollout, reason, operator);
        auditService.logAudit("UPDATE_FLAG", operator, "REST", "key=" + key + ", status=" + status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/metrics")
    public ResponseEntity<String> getMetrics() {
        return ResponseEntity.ok(metricsService.exportPrometheusFormat());
    }
}