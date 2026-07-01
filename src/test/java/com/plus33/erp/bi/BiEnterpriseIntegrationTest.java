package com.plus33.erp.bi;

import com.plus33.erp.bi.alert.AlertEngine;
import com.plus33.erp.bi.dashboard.DashboardCacheService;
import com.plus33.erp.bi.dashboard.KpiEvaluator;
import com.plus33.erp.bi.entity.*;
import com.plus33.erp.bi.etl.ExtractionEngine;
import com.plus33.erp.bi.forecast.ForecastService;
import com.plus33.erp.bi.lineage.DataLineageService;
import com.plus33.erp.bi.maintenance.WarehouseMaintenanceService;
import com.plus33.erp.bi.monitoring.BiOperationalMetrics;
import com.plus33.erp.bi.repository.*;
import com.plus33.erp.bi.scd.ScdType2Engine;
import com.plus33.erp.bi.scheduler.BiConfigurationRegistry;
import com.plus33.erp.bi.scheduler.BiSchedulerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("V40 BI Platform Enterprise Integration Test")
class BiEnterpriseIntegrationTest {

    @Autowired BiEtlJobRepository jobRepo;
    @Autowired BiEtlJobRunRepository jobRunRepo;
    @Autowired BiCdcWatermarkRepository watermarkRepo;
    @Autowired BiKpiDefinitionRepository kpiRepo;
    @Autowired BiKpiFormulaVersionRepository formulaRepo;
    @Autowired BiAnalyticsSnapshotRepository snapshotRepo;
    @Autowired BiAlertRuleRepository alertRuleRepo;
    @Autowired BiExportJobRepository exportJobRepo;
    @Autowired BiEventStoreRepository eventStoreRepo;
    @Autowired BiForecastModelRegistryRepository forecastModelRepo;
    @Autowired BiDataQualityRuleRepository qualityRuleRepo;
    @Autowired BiAnalyticsRoleRepository analyticsRoleRepo;
    @Autowired DimDateRepository dimDateRepo;
    @Autowired BiFactLoadAuditRepository factLoadAuditRepo;
    @Autowired BiSchedulerService schedulerService;
    @Autowired BiConfigurationRegistry configRegistry;
    @Autowired ExtractionEngine extractionEngine;
    @Autowired ScdType2Engine scdEngine;
    @Autowired KpiEvaluator kpiEvaluator;
    @Autowired ForecastService forecastService;
    @Autowired AlertEngine alertEngine;
    @Autowired DashboardCacheService cacheService;
    @Autowired DataLineageService lineageService;
    @Autowired WarehouseMaintenanceService maintenanceService;
    @Autowired BiOperationalMetrics operationalMetrics;
    @Autowired JdbcTemplate jdbc;

    private BiEtlJob createJob(String name) {
        BiEtlJob j = new BiEtlJob();
        j.setJobName(name); j.setSourceModule("TEST");
        j.setSourceTable("stg_finance"); j.setTargetTable("fact_finance");
        return jobRepo.save(j);
    }

    @Test @Order(1) @DisplayName("T001: Warehouse version seeded")
    void t001() { assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM bi_warehouse_version", Integer.class)).isGreaterThan(0); }

    @Test @Order(2) @DisplayName("T002: CDC batch size config = 5000")
    void t002() { assertThat(configRegistry.getCdcBatchSize()).isEqualTo(5000); }

    @Test @Order(3) @DisplayName("T003: dim_date 2020-2027 populated")
    void t003() { assertThat(dimDateRepo.count()).isGreaterThanOrEqualTo(365L * 7); }

    @Test @Order(4) @DisplayName("T004: dim_date lookup by date_key 20240101")
    void t004() { assertThat(dimDateRepo.findByDateKey(20240101).map(DimDate::getYearNumber)).contains(2024); }

    @Test @Order(5) @DisplayName("T005: dim_date lookup by full date 2025-06-15")
    void t005() { assertThat(dimDateRepo.findByFullDate(LocalDate.of(2025, 6, 15)).map(DimDate::getQuarterNumber)).contains(2); }

    @Test @Order(6) @DisplayName("T006: All 10 fact tables exist")
    void t006() {
        List.of("fact_finance","fact_sales","fact_inventory","fact_payroll","fact_manufacturing",
                "fact_procurement","fact_project_financials","fact_hcm","fact_crm","fact_grc")
            .forEach(t -> assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM " + t, Integer.class)).isGreaterThanOrEqualTo(0));
    }

    @Test @Order(7) @DisplayName("T007: All 10 staging tables exist")
    void t007() {
        List.of("stg_finance","stg_sales","stg_inventory","stg_payroll","stg_manufacturing",
                "stg_crm","stg_procurement","stg_projects","stg_hcm","stg_grc")
            .forEach(t -> assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM " + t, Integer.class)).isGreaterThanOrEqualTo(0));
    }

    @Test @Order(8) @DisplayName("T008: USD base currency seeded")
    void t008() {
        String sql = "SELECT COUNT(*) FROM dim_currency WHERE currency_code = 'USD' AND is_base_currency = true";
        assertThat(jdbc.queryForObject(sql, Integer.class)).isEqualTo(1);
    }

    @Test @Order(9) @DisplayName("T009: REVENUE_LINEAR forecast model seeded")
    void t009() { assertThat(forecastModelRepo.findByModelCode("REVENUE_LINEAR")).isPresent(); }

    @Test @Order(10) @DisplayName("T010: 11+ scheduled jobs seeded")
    void t010() { assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM bi_scheduled_job", Integer.class)).isGreaterThanOrEqualTo(11); }

    @Test @Order(11) @DisplayName("T011: ETL job created with CREATED status")
    void t011() { assertThat(createJob("j_" + System.nanoTime()).getStatus()).isEqualTo("CREATED"); }

    @Test @Order(12) @DisplayName("T012: Queue run transitions to QUEUED")
    void t012() {
        BiEtlJobRun r = schedulerService.createAndQueueRun(createJob("q_" + System.nanoTime()).getId(), "t");
        assertThat(r.getStatus()).isEqualTo("QUEUED");
        assertThat(r.getBatchId()).isNotNull();
    }

    @Test @Order(13) @DisplayName("T013: Transition QUEUED -> EXTRACTING")
    void t013() {
        BiEtlJobRun r = schedulerService.createAndQueueRun(createJob("e_" + System.nanoTime()).getId(), "t");
        schedulerService.transitionRun(r.getId(), "EXTRACTING");
        assertThat(jobRunRepo.findById(r.getId()).orElseThrow().getStatus()).isEqualTo("EXTRACTING");
    }

    @Test @Order(14) @DisplayName("T014: Full lifecycle QUEUED->COMPLETED")
    void t014() {
        BiEtlJobRun r = schedulerService.createAndQueueRun(createJob("sm_" + System.nanoTime()).getId(), "t");
        for (String s : List.of("EXTRACTING","VALIDATING","TRANSFORMING","LOADING","COMPLETED"))
            schedulerService.transitionRun(r.getId(), s);
        BiEtlJobRun done = jobRunRepo.findById(r.getId()).orElseThrow();
        assertThat(done.getStatus()).isEqualTo("COMPLETED");
        assertThat(done.getCompletedAt()).isNotNull();
    }

    @Test @Order(15) @DisplayName("T015: Failed run has FAILED status + error message")
    void t015() {
        BiEtlJobRun r = schedulerService.createAndQueueRun(createJob("f_" + System.nanoTime()).getId(), "t");
        schedulerService.failRun(r.getId(), "Timeout");
        assertThat(jobRunRepo.findById(r.getId()).orElseThrow().getErrorMessage()).contains("Timeout");
    }

    @Test @Order(16) @DisplayName("T016: 3 runs tracked per job")
    void t016() {
        BiEtlJob j = createJob("mr_" + System.nanoTime());
        schedulerService.createAndQueueRun(j.getId(), "r1");
        schedulerService.createAndQueueRun(j.getId(), "r2");
        schedulerService.createAndQueueRun(j.getId(), "r3");
        assertThat(jobRunRepo.findByJobIdOrderByCreatedAtDesc(j.getId())).hasSize(3);
    }

    @Test @Order(17) @DisplayName("T017: Active run count is non-negative")
    void t017() { assertThat(schedulerService.countActiveRuns()).isGreaterThanOrEqualTo(0); }

    @Test @Order(18) @DisplayName("T018: ETL job dependency recorded")
    void t018() {
        BiEtlJob p = createJob("p_" + System.nanoTime()), c = createJob("c_" + System.nanoTime());
        jdbc.update("INSERT INTO bi_etl_job_dependency(parent_job_id,child_job_id,dependency_type,execution_order) VALUES(?,?,'MANDATORY',1)", p.getId(), c.getId());
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM bi_etl_job_dependency WHERE parent_job_id = " + p.getId(), Integer.class)).isEqualTo(1);
    }

    @Test @Order(19) @DisplayName("T019: Retry count persisted on run")
    void t019() {
        BiEtlJobRun r = schedulerService.createAndQueueRun(createJob("rt_" + System.nanoTime()).getId(), "t");
        r.setRetryCount(2);
        assertThat(jobRunRepo.save(r).getRetryCount()).isEqualTo(2);
    }

    @Test @Order(20) @DisplayName("T020: Fact load audit persists")
    void t020() {
        BiFactLoadAudit a = new BiFactLoadAudit();
        a.setFactTable("fact_finance"); a.setBatchId("b_" + System.nanoTime()); a.setRowsInserted(500); a.setStatus("COMPLETED");
        assertThat(factLoadAuditRepo.save(a).getId()).isNotNull();
    }

    @Test @Order(21) @DisplayName("T021: ETL batch log written successfully")
    void t021() {
        jdbc.update("INSERT INTO bi_etl_batch_log(batch_id,source_module,source_table,records_extracted,records_staged,status) VALUES(?,'FINANCE','stg_finance',1000,1000,'COMPLETED')", "b_" + System.nanoTime());
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM bi_etl_batch_log WHERE source_module = 'FINANCE'", Integer.class)).isGreaterThanOrEqualTo(1);
    }

    @Test @Order(22) @DisplayName("T022: Warehouse build manifest written")
    void t022() {
        jdbc.update("INSERT INTO bi_warehouse_build_manifest(warehouse_version,migration_range,build_status) VALUES('40.0.1','V168-V180','COMPLETED')");
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM bi_warehouse_build_manifest", Integer.class)).isGreaterThanOrEqualTo(1);
    }

    @Test @Order(23) @DisplayName("T023: Disabled job excluded from enabled query")
    void t023() {
        BiEtlJob j = createJob("dis_" + System.nanoTime()); j.setEnabled(false); jobRepo.save(j);
        assertThat(jobRepo.findByStatusAndEnabledTrue("CREATED").stream().noneMatch(x -> x.getJobName().equals(j.getJobName()))).isTrue();
    }

    @Test @Order(24) @DisplayName("T024: ETL timeout minutes persisted")
    void t024() { BiEtlJob j = createJob("to_" + System.nanoTime()); j.setTimeoutMinutes(120); assertThat(jobRepo.save(j).getTimeoutMinutes()).isEqualTo(120); }

    @Test @Order(25) @DisplayName("T025: ETL job query by source module")
    void t025() {
        BiEtlJob j = createJob("m_" + System.nanoTime()); j.setSourceModule("MOD_X"); jobRepo.save(j);
        assertThat(jobRepo.findBySourceModuleAndEnabledTrue("MOD_X")).isNotEmpty();
    }

    @Test @Order(26) @DisplayName("T026: First extraction watermark starts at 0")
    void t026() { assertThat(extractionEngine.extract(99999L, "TM", "tbl_" + System.nanoTime()).getWatermarkFrom()).isEqualTo(0L); }

    @Test @Order(27) @DisplayName("T027: Watermark advances after extraction")
    void t027() {
        String m = "WM_" + System.nanoTime();
        extractionEngine.advanceWatermark(m, "t1", 1000L, 500);
        assertThat(watermarkRepo.findBySourceModuleAndSourceTable(m, "t1").map(BiCdcWatermark::getLastEventId)).contains(1000L);
    }

    @Test @Order(28) @DisplayName("T028: Second extraction picks up prior watermark")
    void t028() {
        String m = "IC_" + System.nanoTime();
        extractionEngine.advanceWatermark(m, "t1", 500L, 100);
        assertThat(extractionEngine.extract(99998L, m, "t1").getWatermarkFrom()).isEqualTo(500L);
    }

    @Test @Order(29) @DisplayName("T029: Watermark upsert keeps single row")
    void t029() {
        String m = "UP_" + System.nanoTime();
        extractionEngine.advanceWatermark(m, "t1", 100L, 10);
        extractionEngine.advanceWatermark(m, "t1", 200L, 20);
        assertThat(watermarkRepo.findBySourceModuleAndSourceTable(m, "t1").map(BiCdcWatermark::getLastEventId)).contains(200L);
    }

    @Test @Order(30) @DisplayName("T030: CDC watermark status defaults ACTIVE")
    void t030() {
        String m = "AC_" + System.nanoTime();
        extractionEngine.advanceWatermark(m, "t1", 1L, 1);
        assertThat(watermarkRepo.findBySourceModuleAndSourceTable(m, "t1").map(BiCdcWatermark::getStatus)).contains("ACTIVE");
    }

    @Test @Order(31) @DisplayName("T031: SCD dimensions list = 9")
    void t031() { assertThat(scdEngine.getScdDimensions()).hasSize(9); }

    @Test @Order(32) @DisplayName("T032: SCD violations empty for dim_customer")
    void t032() { assertThat(scdEngine.detectViolations("dim_customer", "customer_id")).isEmpty(); }

    @Test @Order(33) @DisplayName("T033: SCD expire sets is_current=false")
    void t033() {
        Long pid = Math.abs(System.nanoTime() % 1000000);
        jdbc.update("INSERT INTO dim_product(company_id,product_id,product_code,product_name,effective_from,is_current) VALUES(1,?,'SCD-P','SCD Product',CURRENT_DATE,true)", pid);
        assertThat(scdEngine.expireCurrentRecord("dim_product", "product_id", pid)).isEqualTo(1);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM dim_product WHERE product_id = " + pid + " AND is_current = true", Integer.class)).isEqualTo(0);
    }

    @Test @Order(34) @DisplayName("T034: 20+ standard KPIs seeded")
    void t034() { assertThat(kpiEvaluator.getAllActiveKpis()).hasSizeGreaterThanOrEqualTo(20); }

    @Test @Order(35) @DisplayName("T035: REVENUE KPI in FINANCE category")
    void t035() { assertThat(kpiEvaluator.getActiveKpi("REVENUE").map(BiKpiDefinition::getKpiCategory)).contains("FINANCE"); }

    @Test @Order(36) @DisplayName("T036: REVENUE KPI has current formula")
    void t036() { BiKpiDefinition k = kpiEvaluator.getActiveKpi("REVENUE").orElseThrow(); assertThat(kpiEvaluator.getActiveFormula(k.getId())).isPresent(); }

    @Test @Order(37) @DisplayName("T037: GROSS_MARGIN_PCT formula version exists")
    void t037() { BiKpiDefinition k = kpiEvaluator.getActiveKpi("GROSS_MARGIN_PCT").orElseThrow(); assertThat(formulaRepo.findByKpiIdAndIsCurrentTrue(k.getId())).isPresent(); }

    @Test @Order(38) @DisplayName("T038: KPI health HEALTHY above threshold")
    void t038() {
        BiKpiDefinition k = kpiEvaluator.getActiveKpi("REVENUE").orElseThrow();
        k.setDirection("HIGHER"); k.setThresholdCritical(new BigDecimal("100000")); k.setThresholdWarning(new BigDecimal("500000"));
        assertThat(kpiEvaluator.evaluateHealth(k, new BigDecimal("1000000"))).isEqualTo(KpiEvaluator.KpiHealthStatus.HEALTHY);
    }

    @Test @Order(39) @DisplayName("T039: KPI health CRITICAL below threshold")
    void t039() {
        BiKpiDefinition k = kpiEvaluator.getActiveKpi("REVENUE").orElseThrow();
        k.setDirection("HIGHER"); k.setThresholdCritical(new BigDecimal("500000"));
        assertThat(kpiEvaluator.evaluateHealth(k, new BigDecimal("100000"))).isEqualTo(KpiEvaluator.KpiHealthStatus.CRITICAL);
    }

    @Test @Order(40) @DisplayName("T040: KPI dependency graph has 5+ edges")
    void t040() { assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM bi_kpi_dependency", Integer.class)).isGreaterThanOrEqualTo(5); }

    @Test @Order(41) @DisplayName("T041: Custom KPI created")
    void t041() {
        BiKpiDefinition k = new BiKpiDefinition();
        k.setKpiCode("TK_" + System.nanoTime()); k.setKpiName("T"); k.setStatus("ACTIVE"); k.setDirection("HIGHER");
        assertThat(kpiRepo.save(k).getId()).isNotNull();
    }

    @Test @Order(42) @DisplayName("T042: Forecast run created with 6 predictions")
    void t042() {
        ForecastService.ForecastRunResult r = forecastService.executeForecast(1L, "REVENUE", 6);
        assertThat(r.runId()).isNotNull(); assertThat(r.points()).hasSize(6);
    }

    @Test @Order(43) @DisplayName("T043: Forecast predictions have valid upper >= lower bounds")
    void t043() {
        forecastService.executeForecast(1L, "DEMAND", 3).points()
            .forEach(p -> { assertThat(p.value()).isPositive(); assertThat(p.upper()).isGreaterThanOrEqualTo(p.lower()); });
    }

    @Test @Order(44) @DisplayName("T044: Unknown forecast domain returns empty")
    void t044() { assertThat(forecastService.executeForecast(1L, "UNKNOWN_DOMAIN_XYZ", 6).points()).isEmpty(); }

    @Test @Order(45) @DisplayName("T045: Alert rule created")
    void t045() {
        BiAlertRule r = new BiAlertRule();
        r.setRuleName("R_" + System.nanoTime()); r.setConditionType("BELOW_THRESHOLD"); r.setThresholdValue(new BigDecimal("100000")); r.setSeverity("CRITICAL");
        assertThat(alertRuleRepo.save(r).getId()).isNotNull();
    }

    @Test @Order(46) @DisplayName("T046: Alert fires for BELOW_THRESHOLD breach")
    void t046() {
        jdbc.update("DELETE FROM bi_alert_trigger");
        BiAlertRule r = new BiAlertRule();
        r.setRuleName("BR_" + System.nanoTime()); r.setConditionType("BELOW_THRESHOLD"); r.setThresholdValue(new BigDecimal("500000")); r.setSeverity("WARNING");
        alertRuleRepo.save(r);
        assertThat(alertEngine.evaluateRules(1L, "REVENUE", new BigDecimal("100000"))).isNotEmpty();
    }

    @Test @Order(47) @DisplayName("T047: No alert fires when no matching rules")
    void t047() {
        jdbc.update("DELETE FROM bi_alert_trigger");
        jdbc.update("DELETE FROM bi_alert_rule WHERE rule_name LIKE 'R_%' OR rule_name LIKE 'BR_%'");
        assertThat(alertEngine.evaluateRules(1L, "REVENUE", new BigDecimal("9999999"))).isEmpty();
    }

    @Test @Order(48) @DisplayName("T048: Config CDC_BATCH_SIZE = 5000")
    void t048() { assertThat(configRegistry.getCdcBatchSize()).isEqualTo(5000); }

    @Test @Order(49) @DisplayName("T049: Config MAX_RETRY_COUNT = 3")
    void t049() { assertThat(configRegistry.getMaxRetryCount()).isEqualTo(3); }

    @Test @Order(50) @DisplayName("T050: Config DEFAULT_FORECAST_MODEL = LINEAR")
    void t050() { assertThat(configRegistry.getDefaultForecastModel()).isEqualTo("LINEAR"); }

    @Test @Order(51) @DisplayName("T051: Missing config key returns default")
    void t051() { assertThat(configRegistry.getString("MISSING_KEY_XYZ", "DFLT")).isEqualTo("DFLT"); }

    @Test @Order(52) @DisplayName("T052: ETL group config contains CDC_BATCH_SIZE")
    void t052() { assertThat(configRegistry.getAllByGroup("ETL")).containsKey("CDC_BATCH_SIZE"); }

    @Test @Order(53) @DisplayName("T053: Cache put+get within TTL")
    void t053() { cacheService.put(1L, "k:rev", "1M", 300); assertThat(cacheService.get(1L, "k:rev")).isPresent().contains("1M"); }

    @Test @Order(54) @DisplayName("T054: Cache miss for unknown key")
    void t054() { assertThat(cacheService.get(1L, "no:key:xyz")).isEmpty(); }

    @Test @Order(55) @DisplayName("T055: Company cache eviction removes all entries")
    void t055() {
        cacheService.put(88L, "a", "va", 300); cacheService.put(88L, "b", "vb", 300);
        assertThat(cacheService.evictCompany(88L)).isEqualTo(2);
        assertThat(cacheService.get(88L, "a")).isEmpty();
    }

    @Test @Order(56) @DisplayName("T056: Data lineage record created")
    void t056() { assertThat(lineageService.recordLineage("FINANCE", "stg_finance", "fact_finance", "ETL", null, "b123", 1000)).isNotNull(); }

    @Test @Order(57) @DisplayName("T057: 2 lineage steps added to lineage record")
    void t057() {
        Long id = lineageService.recordLineage("SALES", "stg_sales", "fact_sales", "ETL", null, "b456", 500);
        lineageService.addLineageStep(id, 1, "EXTRACT", "EXTRACTION", "stg_sales", null, 500, 500);
        lineageService.addLineageStep(id, 2, "TRANSFORM", "TRANSFORMATION", "stg_sales", "fact_sales", 500, 490);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM bi_lineage_step WHERE lineage_id = " + id, Integer.class)).isEqualTo(2);
    }

    @Test @Order(58) @DisplayName("T058: Staging purge completes")
    void t058() { assertThat(maintenanceService.purgeStagingData(30).operation()).isEqualTo("STAGING_PURGE"); }

    @Test @Order(59) @DisplayName("T059: Cache eviction maintenance completes")
    void t059() { assertThat(maintenanceService.purgeExpiredCache().operation()).isEqualTo("CACHE_EVICTION"); }

    @Test @Order(60) @DisplayName("T060: ETL log purge completes")
    void t060() { assertThat(maintenanceService.purgeOldEtlLogs(180).operation()).isEqualTo("ETL_LOG_PURGE"); }

    @Test @Order(61) @DisplayName("T061: System health recorded and status valid")
    void t061() {
        operationalMetrics.recordSystemHealth();
        assertThat(operationalMetrics.getLatestHealth().overallStatus()).isIn("HEALTHY", "WARNING", "DEGRADED");
    }

    @Test @Order(62) @DisplayName("T062: System health fields non-negative")
    void t062() {
        BiOperationalMetrics.SystemHealthSummary h = operationalMetrics.getLatestHealth();
        assertThat(h.runningJobs()).isGreaterThanOrEqualTo(0); assertThat(h.failedJobs24h()).isGreaterThanOrEqualTo(0);
    }

    @Test @Order(63) @DisplayName("T063: Analytics snapshot created")
    void t063() {
        BiAnalyticsSnapshot s = new BiAnalyticsSnapshot();
        s.setCompanyId(1L); s.setSnapshotDate(LocalDate.now()); s.setSnapshotPeriod("MONTHLY"); s.setKpiCode("SNAP"); s.setKpiValue(new BigDecimal("999999"));
        assertThat(snapshotRepo.save(s).getId()).isNotNull();
    }

    @Test @Order(64) @DisplayName("T064: Snapshots returned desc by date")
    void t064() {
        String c = "Q_" + System.nanoTime();
        for (int i = 0; i < 3; i++) {
            BiAnalyticsSnapshot s = new BiAnalyticsSnapshot();
            s.setCompanyId(2L); s.setSnapshotDate(LocalDate.now().minusMonths(i)); s.setSnapshotPeriod("MONTHLY");
            s.setKpiCode(c); s.setKpiValue(BigDecimal.valueOf(100000 + i * 10000));
            snapshotRepo.save(s);
        }
        List<BiAnalyticsSnapshot> r = snapshotRepo.findByCompanyIdAndKpiCodeOrderBySnapshotDateDesc(2L, c);
        assertThat(r).hasSize(3);
        assertThat(r.get(0).getSnapshotDate()).isAfterOrEqualTo(r.get(1).getSnapshotDate());
    }

    @Test @Order(65) @DisplayName("T065: Export job created with REQUESTED status")
    void t065() {
        BiExportJob j = new BiExportJob();
        j.setCompanyId(1L); j.setJobReference("EXP-" + System.nanoTime()); j.setReportName("Exec"); j.setExportFormat("PDF"); j.setRequestedBy("admin");
        assertThat(exportJobRepo.save(j).getStatus()).isEqualTo("REQUESTED");
    }

    @Test @Order(66) @DisplayName("T066: Export job found by reference")
    void t066() {
        String ref = "REF-" + System.nanoTime();
        BiExportJob j = new BiExportJob(); j.setCompanyId(1L); j.setJobReference(ref); j.setReportName("F"); j.setExportFormat("EXCEL"); j.setRequestedBy("u1");
        exportJobRepo.save(j);
        assertThat(exportJobRepo.findByJobReference(ref).map(BiExportJob::getExportFormat)).contains("EXCEL");
    }

    @Test @Order(67) @DisplayName("T067: 6+ analytics roles seeded")
    void t067() { assertThat(analyticsRoleRepo.count()).isGreaterThanOrEqualTo(6); }

    @Test @Order(68) @DisplayName("T068: BI_EXECUTIVE role is active")
    void t068() { assertThat(analyticsRoleRepo.findByRoleCode("BI_EXECUTIVE").map(BiAnalyticsRole::getIsActive)).contains(true); }

    @Test @Order(69) @DisplayName("T069: BI_ANALYST role exists")
    void t069() { assertThat(analyticsRoleRepo.findByRoleCode("BI_ANALYST")).isPresent(); }

    @Test @Order(70) @DisplayName("T070: 30+ BI permissions seeded")
    void t070() { assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM permissions WHERE code LIKE 'bi:%'", Integer.class)).isGreaterThanOrEqualTo(30); }

    @Test @Order(71) @DisplayName("T071: BI event store record persists")
    void t071() {
        BiEventStore e = new BiEventStore();
        e.setCompanyId(1L); e.setEventType("KPI_CALC"); e.setIdempotencyKey("idem-" + System.nanoTime()); e.setPerformedBy("sched");
        assertThat(eventStoreRepo.save(e).getId()).isNotNull();
    }

    @Test @Order(72) @DisplayName("T072: Idempotency key unique constraint enforced")
    void t072() {
        String k = "uk-" + System.nanoTime();
        BiEventStore e1 = new BiEventStore(); e1.setEventType("T"); e1.setIdempotencyKey(k);
        eventStoreRepo.save(e1); eventStoreRepo.flush();
        Assertions.assertThrows(Exception.class, () -> {
            BiEventStore e2 = new BiEventStore(); e2.setEventType("T"); e2.setIdempotencyKey(k);
            eventStoreRepo.saveAndFlush(e2);
        });
    }

    @Test @Order(73) @DisplayName("T073: Warehouse version 40.0.0 seeded")
    void t073() { assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM bi_warehouse_version WHERE warehouse_version = '40.0.0'", Integer.class)).isEqualTo(1); }

    @Test @Order(74) @DisplayName("T074: 8+ feature flags seeded")
    void t074() { assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM bi_feature_flag", Integer.class)).isGreaterThanOrEqualTo(8); }

    @Test @Order(75) @DisplayName("T075: FORECASTING feature flag enabled")
    void t075() { assertThat(jdbc.queryForObject("SELECT is_enabled FROM bi_feature_flag WHERE feature_code = 'FORECASTING'", Boolean.class)).isTrue(); }

    @Test @Order(76) @DisplayName("T076: mv_finance_monthly view queryable")
    void t076() { assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM mv_finance_monthly", Integer.class)).isGreaterThanOrEqualTo(0); }

    @Test @Order(77) @DisplayName("T077: mv_sales_monthly view queryable")
    void t077() { assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM mv_sales_monthly", Integer.class)).isGreaterThanOrEqualTo(0); }

    @Test @Order(78) @DisplayName("T078: mv_executive_summary view queryable")
    void t078() { assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM mv_executive_summary", Integer.class)).isGreaterThanOrEqualTo(0); }

    @Test @Order(79) @DisplayName("T079: mv_manufacturing_monthly view queryable")
    void t079() { assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM mv_manufacturing_monthly", Integer.class)).isGreaterThanOrEqualTo(0); }

    @Test @Order(80) @DisplayName("T080: mv_hcm_monthly view queryable")
    void t080() { assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM mv_hcm_monthly", Integer.class)).isGreaterThanOrEqualTo(0); }
}