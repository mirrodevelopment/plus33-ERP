package com.plus33.erp.bi.controller;

import com.plus33.erp.bi.dashboard.KpiEvaluator;
import com.plus33.erp.bi.entity.BiAnalyticsSnapshot;
import com.plus33.erp.bi.entity.BiKpiDefinition;
import com.plus33.erp.bi.monitoring.BiOperationalMetrics;
import com.plus33.erp.bi.repository.BiAnalyticsSnapshotRepository;
import com.plus33.erp.bi.repository.BiKpiDefinitionRepository;
import com.plus33.erp.bi.scheduler.BiSchedulerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bi")
public class BiDashboardController {

    private final KpiEvaluator kpiEvaluator;
    private final BiKpiDefinitionRepository kpiRepo;
    private final BiAnalyticsSnapshotRepository snapshotRepo;
    private final BiSchedulerService schedulerService;
    private final BiOperationalMetrics operationalMetrics;

    public BiDashboardController(KpiEvaluator kpiEvaluator, BiKpiDefinitionRepository kpiRepo,
                                  BiAnalyticsSnapshotRepository snapshotRepo,
                                  BiSchedulerService schedulerService,
                                  BiOperationalMetrics operationalMetrics) {
        this.kpiEvaluator = kpiEvaluator;
        this.kpiRepo = kpiRepo;
        this.snapshotRepo = snapshotRepo;
        this.schedulerService = schedulerService;
        this.operationalMetrics = operationalMetrics;
    }

    @GetMapping("/kpis")
    public ResponseEntity<List<BiKpiDefinition>> getAllActiveKpis() {
        return ResponseEntity.ok(kpiEvaluator.getAllActiveKpis());
    }

    @GetMapping("/kpis/{kpiCode}")
    public ResponseEntity<BiKpiDefinition> getKpi(@PathVariable String kpiCode) {
        return kpiEvaluator.getActiveKpi(kpiCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/snapshots/{companyId}/{kpiCode}")
    public ResponseEntity<List<BiAnalyticsSnapshot>> getSnapshots(
            @PathVariable Long companyId,
            @PathVariable String kpiCode) {
        return ResponseEntity.ok(snapshotRepo.findByCompanyIdAndKpiCodeOrderBySnapshotDateDesc(companyId, kpiCode));
    }

    @GetMapping("/snapshots/{companyId}/range")
    public ResponseEntity<List<BiAnalyticsSnapshot>> getSnapshotsByRange(
            @PathVariable Long companyId,
            @RequestParam String from,
            @RequestParam String to) {
        return ResponseEntity.ok(snapshotRepo.findByCompanyIdAndSnapshotDateBetween(
                companyId, LocalDate.parse(from), LocalDate.parse(to)));
    }

    @GetMapping("/system/health")
    public ResponseEntity<BiOperationalMetrics.SystemHealthSummary> getSystemHealth() {
        return ResponseEntity.ok(operationalMetrics.getLatestHealth());
    }

    @GetMapping("/system/stats")
    public ResponseEntity<Map<String, Object>> getSystemStats() {
        return ResponseEntity.ok(Map.of(
            "activeRuns", schedulerService.countActiveRuns(),
            "totalKpis", kpiRepo.count()
        ));
    }
}
