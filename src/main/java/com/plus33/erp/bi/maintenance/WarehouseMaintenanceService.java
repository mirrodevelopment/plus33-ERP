package com.plus33.erp.bi.maintenance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * WarehouseMaintenanceService: Periodic warehouse operations.
 * - Purges expired staging records
 * - Evicts expired dashboard cache entries
 * - Logs all maintenance runs to bi_maintenance_log
 */
@Service
public class WarehouseMaintenanceService {

    private static final Logger log = LoggerFactory.getLogger(WarehouseMaintenanceService.class);
    private final JdbcTemplate jdbc;

    public WarehouseMaintenanceService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    public MaintenanceResult purgeStagingData(int retentionDays) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(retentionDays);
        LocalDateTime started = LocalDateTime.now();
        List<String> stagingTables = List.of(
            "stg_finance","stg_sales","stg_inventory","stg_payroll",
            "stg_manufacturing","stg_crm","stg_procurement","stg_projects","stg_hcm","stg_grc"
        );
        int totalPurged = 0;
        for (String table : stagingTables) {
            int rows = jdbc.update("DELETE FROM " + table + " WHERE loaded_at < ? AND load_status IN ('LOADED','REJECTED')", cutoff);
            totalPurged += rows;
        }
        long duration = java.time.Duration.between(started, LocalDateTime.now()).toMillis();
        logMaintenance("STAGING_PURGE", null, null, started, LocalDateTime.now(), duration, (long) totalPurged);
        log.info("[MAINTENANCE] Staging purge complete: {} rows removed, cutoff={}", totalPurged, cutoff);
        return new MaintenanceResult("STAGING_PURGE", totalPurged, duration);
    }

    @Transactional
    public MaintenanceResult purgeExpiredCache() {
        LocalDateTime started = LocalDateTime.now();
        int count = jdbc.update("DELETE FROM bi_dashboard_cache WHERE expires_at < CURRENT_TIMESTAMP");
        long duration = java.time.Duration.between(started, LocalDateTime.now()).toMillis();
        logMaintenance("CACHE_EVICTION", null, null, started, LocalDateTime.now(), duration, (long) count);
        log.info("[MAINTENANCE] Cache eviction complete: {} entries removed.", count);
        return new MaintenanceResult("CACHE_EVICTION", count, duration);
    }

    @Transactional
    public MaintenanceResult purgeOldEtlLogs(int retentionDays) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(retentionDays);
        LocalDateTime started = LocalDateTime.now();
        int count = jdbc.update("DELETE FROM bi_etl_batch_log WHERE created_at < ? AND status IN ('COMPLETED','FAILED')", cutoff);
        long duration = java.time.Duration.between(started, LocalDateTime.now()).toMillis();
        logMaintenance("ETL_LOG_PURGE", null, null, started, LocalDateTime.now(), duration, (long) count);
        log.info("[MAINTENANCE] ETL log purge complete: {} rows removed.", count);
        return new MaintenanceResult("ETL_LOG_PURGE", count, duration);
    }

    private void logMaintenance(String opType, String targetTable, String partition,
                                 LocalDateTime started, LocalDateTime completed,
                                 Long durationMs, Long rowsAffected) {
        jdbc.update("""
            INSERT INTO bi_maintenance_log(operation_type, target_table, target_partition,
                started_at, completed_at, duration_ms, rows_affected, status, executed_by, created_at)
            VALUES (?,?,?,?,?,?,?,'COMPLETED','scheduler',CURRENT_TIMESTAMP)
            """, opType, targetTable, partition, started, completed, durationMs, rowsAffected);
    }

    public record MaintenanceResult(String operation, int rowsAffected, long durationMs) {}
}
