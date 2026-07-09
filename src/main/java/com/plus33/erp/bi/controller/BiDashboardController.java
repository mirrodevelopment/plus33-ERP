/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.controller
 * File              : BiDashboardController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiDashboardController
 * Related Service   : BiDashboardControllerService, BiDashboardControllerServiceImpl
 * Related Repository: BiKpiDefinitionRepository, BiAnalyticsSnapshotRepository
 * Related Entity    : BiDashboardController
 * Related DTO       : N/A
 * Related Mapper    : BiDashboardControllerMapper
 * Related DB Table  : bi_dashboard_controllers
 * Related REST APIs : GET /api/bi/kpis, GET /api/bi/kpis/{kpiCode}, GET /api/bi/snapshots/{companyId}/{kpiCode}, GET /api/bi/snapshots/{companyId}/range
 * Depends On        : None
 * Used By           : Bi Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Bi Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: GET /api/bi/kpis, GET /api/bi/kpis/{kpiCode}, GET /api/bi/snapshots/{companyId}/{kpiCode}, GET /api/bi/snapshots/{companyId}/range
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiDashboardController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to BiDashboardService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> BiDashboardController.endpoint()
 *   --> BiDashboardService.method()
 *   --> BiDashboardRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> GET /api/bi/kpis, GET /api/bi/kpis/{kpiCode}, GET /api/bi/snapshots/{companyId}/{kpiCode}, GET /api/bi/snapshots/{companyId}/range, GET /api/bi/system/health</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Retrieves a paginated list of all active kpis records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/kpis")
    public ResponseEntity<List<BiKpiDefinition>> getAllActiveKpis() {
        return ResponseEntity.ok(kpiEvaluator.getAllActiveKpis());
    }

    /**
     * Retrieves kpi data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param kpiCode the kpiCode input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/kpis/{kpiCode}")
    public ResponseEntity<BiKpiDefinition> getKpi(@PathVariable String kpiCode) {
        return kpiEvaluator.getActiveKpi(kpiCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves snapshots data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/snapshots/{companyId}/{kpiCode}")
    public ResponseEntity<List<BiAnalyticsSnapshot>> getSnapshots(
            @PathVariable Long companyId,
            @PathVariable String kpiCode) {
        return ResponseEntity.ok(snapshotRepo.findByCompanyIdAndKpiCodeOrderBySnapshotDateDesc(companyId, kpiCode));
    }

    /**
     * Retrieves snapshots by range data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/snapshots/{companyId}/range")
    public ResponseEntity<List<BiAnalyticsSnapshot>> getSnapshotsByRange(
            @PathVariable Long companyId,
            @RequestParam String from,
            @RequestParam String to) {
        return ResponseEntity.ok(snapshotRepo.findByCompanyIdAndSnapshotDateBetween(
                companyId, LocalDate.parse(from), LocalDate.parse(to)));
    }

    /**
     * Retrieves system health data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/system/health")
    public ResponseEntity<BiOperationalMetrics.SystemHealthSummary> getSystemHealth() {
        return ResponseEntity.ok(operationalMetrics.getLatestHealth());
    }

    /**
     * Retrieves system stats data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/system/stats")
    public ResponseEntity<Map<String, Object>> getSystemStats() {
        return ResponseEntity.ok(Map.of(
            "activeRuns", schedulerService.countActiveRuns(),
            "totalKpis", kpiRepo.count()
        ));
    }
}