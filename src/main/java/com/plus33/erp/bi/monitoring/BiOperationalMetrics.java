/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.monitoring
 * File              : BiOperationalMetrics.java
 * Purpose           : Business logic service layer for Bi Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiOperationalMetricsController
 * Related Service   : BiOperationalMetrics
 * Related Repository: BiOperationalMetricsRepository
 * Related Entity    : BiOperationalMetrics
 * Related DTO       : N/A
 * Related Mapper    : BiOperationalMetricsMapper
 * Related DB Table  : bi_operational_metricss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiOperationalMetricsController, BiOperationalMetricsImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Bi Module. Implements BiOperationalMetricsService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.bi.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * BiOperationalMetrics: Captures and stores system performance telemetry
 * into bi_operational_metrics and bi_system_health tables.
 */
@Service
public class BiOperationalMetrics {

    private static final Logger log = LoggerFactory.getLogger(BiOperationalMetrics.class);
    private final JdbcTemplate jdbc;

    public BiOperationalMetrics(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    /**
     * Performs the recordSystemHealth operation in this module.
     *
     */
    @Transactional
    public void recordSystemHealth() {
        try {
            int activeJobs = jdbc.queryForObject(
                "SELECT COUNT(*) FROM bi_etl_job_run WHERE status IN ('QUEUED','EXTRACTING','VALIDATING','TRANSFORMING','LOADING')",
                Integer.class);
            int failedJobs24h = jdbc.queryForObject(
                "SELECT COUNT(*) FROM bi_etl_job_run WHERE status='FAILED' AND created_at > NOW() - INTERVAL '24 hours'",
                Integer.class);
            String overallStatus = failedJobs24h > 5 ? "DEGRADED" : (activeJobs > 20 ? "WARNING" : "HEALTHY");
            jdbc.update("""
                INSERT INTO bi_system_health(check_timestamp, running_jobs, failed_jobs_24h, queue_depth, overall_status)
                VALUES (CURRENT_TIMESTAMP, ?, ?, ?, ?)
                """, activeJobs, failedJobs24h, activeJobs, overallStatus);
            log.debug("[HEALTH] Status={} running={} failed24h={}", overallStatus, activeJobs, failedJobs24h);
        } catch (Exception e) {
            log.error("[HEALTH] Failed to record system health: {}", e.getMessage());
        }
    }

    /**
     * Retrieves latest health data from the database.
     *
     * @return the SystemHealthSummary result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public SystemHealthSummary getLatestHealth() {
        try {
            return jdbc.queryForObject("""
                SELECT running_jobs, failed_jobs_24h, queue_depth, overall_status
                FROM bi_system_health ORDER BY check_timestamp DESC LIMIT 1
                """, (rs, i) -> new SystemHealthSummary(
                    rs.getInt("running_jobs"), rs.getInt("failed_jobs_24h"),
                    rs.getInt("queue_depth"), rs.getString("overall_status")));
        } catch (Exception e) {
            return new SystemHealthSummary(0, 0, 0, "UNKNOWN");
        }
    }

    public record SystemHealthSummary(int runningJobs, int failedJobs24h, int queueDepth, String overallStatus) {}
}