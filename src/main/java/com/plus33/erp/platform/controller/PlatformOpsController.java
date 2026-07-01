package com.plus33.erp.platform.controller;

import com.plus33.erp.platform.config.DistributedConfigService;
import com.plus33.erp.platform.featureflag.FeatureFlagService;
import com.plus33.erp.platform.metrics.PrometheusExporterService;
import com.plus33.erp.platform.audit.PlatformAuditService;
import com.plus33.erp.platform.cache.DistributedCacheManager;
import com.plus33.erp.platform.lock.DistributedLockManager;
import com.plus33.erp.platform.dashboard.PlatformRuntimeDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/platform")
public class PlatformOpsController {
    @Autowired DistributedConfigService configService;
    @Autowired FeatureFlagService flagService;
    @Autowired PrometheusExporterService metricsService;
    @Autowired PlatformAuditService auditService;
    @Autowired DistributedCacheManager cacheManager;
    @Autowired DistributedLockManager lockManager;
    @Autowired PlatformRuntimeDashboardService dashboardService;

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

    @PostMapping("/cache/evict")
    public ResponseEntity<Void> evictCache(
            @RequestParam String namespace,
            @RequestParam String key,
            @RequestParam String operator) {
        cacheManager.evict(namespace, key);
        auditService.logAudit("EVICT_CACHE", operator, "REST", "ns=" + namespace + ", key=" + key);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/lock")
    public ResponseEntity<Boolean> acquireLock(
            @RequestParam String name,
            @RequestParam String node,
            @RequestParam int ttl) {
        return ResponseEntity.ok(lockManager.acquireLock(name, node, ttl));
    }

    @DeleteMapping("/lock")
    public ResponseEntity<Void> releaseLock(
            @RequestParam String name,
            @RequestParam String node) {
        lockManager.releaseLock(name, node);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }
}