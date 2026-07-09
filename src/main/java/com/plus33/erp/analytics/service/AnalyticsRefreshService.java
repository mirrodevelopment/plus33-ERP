/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.service
 * File              : AnalyticsRefreshService.java
 * Purpose           : Business logic service layer for Analytics Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AnalyticsRefreshController
 * Related Service   : AnalyticsRefreshService
 * Related Repository: AnalyticsRefreshRepository
 * Related Entity    : AnalyticsRefresh
 * Related DTO       : N/A
 * Related Mapper    : AnalyticsRefreshMapper
 * Related DB Table  : analytics_refreshs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AnalyticsRefreshController, AnalyticsRefreshServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Analytics Module. Implements AnalyticsRefreshService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.analytics.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Analytics Module</b>
 *
 * <p><b>Class  :</b> {@code AnalyticsRefreshService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.analytics.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Analytics Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * AnalyticsRefreshController
 *   --> AnalyticsRefreshService (this)
 *   --> Validate business rules
 *   --> AnalyticsRefreshRepository (read/write 'analytics_refreshs')
 *   --> AnalyticsRefreshMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code analytics_refreshs}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class AnalyticsRefreshService {

    private final JdbcTemplate jdbcTemplate;

    public AnalyticsRefreshService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Scheduled(cron = "0 */15 * * * *")
    public void refreshAll() {
        refreshViewWithNewTransaction("mv_sales_daily");
        refreshViewWithNewTransaction("mv_inventory_levels");
        refreshViewWithNewTransaction("mv_attendance_daily");
        refreshViewWithNewTransaction("mv_financial_summary");
        refreshViewWithNewTransaction("mv_procurement_summary");
        refreshViewWithNewTransaction("mv_supplier_performance");
        refreshViewWithNewTransaction("mv_payables_aging");
        refreshViewWithNewTransaction("mv_po_fulfilment");
        refreshViewWithNewTransaction("mv_invoice_matching");
    }

    /**
     * Performs the refreshKpis operation in this module.
     *
     */
    @Scheduled(cron = "0 0,15,30,45 * * * *")
    public void refreshKpis() {
        refreshViewWithNewTransaction("mv_inventory_kpis");
    }

    /**
     * Performs the refreshAging operation in this module.
     *
     */
    @Scheduled(cron = "0 5,20,35,50 * * * *")
    public void refreshAging() {
        refreshViewWithNewTransaction("mv_inventory_aging_expiry");
    }

    /**
     * Performs the refreshAbcXyz operation in this module.
     *
     */
    @Scheduled(cron = "0 10,25,40,55 * * * *")
    public void refreshAbcXyz() {
        refreshDemandSummary();
        refreshViewWithNewTransaction("mv_inventory_abc_xyz");
    }

    /**
     * Performs the refreshTraceability operation in this module.
     *
     */
    @Scheduled(cron = "0 12,27,42,57 * * * *")
    public void refreshTraceability() {
        refreshViewWithNewTransaction("mv_inventory_traceability_metrics");
    }

    /**
     * Performs the refreshReplenishment operation in this module.
     *
     */
    @Scheduled(cron = "0 14,29,44,59 * * * *")
    public void refreshReplenishment() {
        refreshViewWithNewTransaction("mv_inventory_replenishment_metrics");
    }

    /**
     * Performs the refreshTurnover operation in this module.
     *
     */
    @Scheduled(cron = "0 7,22,37,52 * * * *")
    public void refreshTurnover() {
        refreshViewWithNewTransaction("mv_inventory_turnover");
    }

    /**
     * Performs the refreshDemandSummary operation in this module.
     *
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void refreshDemandSummary() {
        try {
            jdbcTemplate.execute("DELETE FROM weekly_product_demand_summary");

            String insertSql = "INSERT INTO weekly_product_demand_summary (company_id, product_id, warehouse_id, store_id, week_start_date, total_quantity) " +
                // Store-level (sales occur at stores)
                "SELECT st.company_id, sti.product_id, NULL::BIGINT, st.store_id, DATE_TRUNC('week', st.transaction_time)::DATE, SUM(sti.quantity) " +
                "FROM sales_transaction_items sti " +
                "JOIN sales_transactions st ON sti.sales_transaction_id = st.id " +
                "WHERE st.status = 'COMPLETED' AND st.transaction_time >= NOW() - INTERVAL '365 days' " +
                "GROUP BY st.company_id, sti.product_id, st.store_id, DATE_TRUNC('week', st.transaction_time)::DATE " +
                "UNION ALL " +
                // Company-level aggregates
                "SELECT st.company_id, sti.product_id, NULL::BIGINT, NULL::BIGINT, DATE_TRUNC('week', st.transaction_time)::DATE, SUM(sti.quantity) " +
                "FROM sales_transaction_items sti " +
                "JOIN sales_transactions st ON sti.sales_transaction_id = st.id " +
                "WHERE st.status = 'COMPLETED' AND st.transaction_time >= NOW() - INTERVAL '365 days' " +
                "GROUP BY st.company_id, sti.product_id, DATE_TRUNC('week', st.transaction_time)::DATE";

            jdbcTemplate.execute(insertSql);
        } catch (Exception e) {
            System.err.println("Failed to refresh weekly product demand summary: " + e.getMessage());
        }
    }

    /**
     * Performs the refreshViewWithNewTransaction operation in this module.
     *
     * @param viewName the viewName input value
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void refreshViewWithNewTransaction(String viewName) {
        long startTime = System.currentTimeMillis();
        String status = "SUCCESS";
        try {
            if (isPopulated(viewName)) {
                jdbcTemplate.execute("REFRESH MATERIALIZED VIEW CONCURRENTLY " + viewName);
            } else {
                jdbcTemplate.execute("REFRESH MATERIALIZED VIEW " + viewName);
            }
        } catch (Exception e) {
            status = "FAILED";
            System.err.println("Failed to refresh materialized view " + viewName + ": " + e.getMessage());
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            try {
                jdbcTemplate.update(
                    "INSERT INTO analytics_refresh_log (view_name, last_refreshed_at, refresh_duration_ms, refresh_status) " +
                    "VALUES (?, NOW(), ?, ?) " +
                    "ON CONFLICT (view_name) DO UPDATE SET last_refreshed_at = EXCLUDED.last_refreshed_at, " +
                    "refresh_duration_ms = EXCLUDED.refresh_duration_ms, refresh_status = EXCLUDED.refresh_status",
                    viewName, duration, status
                );
            } catch (Exception ex) {
                System.err.println("Failed to log refresh status for " + viewName + ": " + ex.getMessage());
            }
        }
    }

    private boolean isPopulated(String viewName) {
        try {
            String sql = "SELECT relispopulated FROM pg_class WHERE relname = ?";
            Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, viewName);
            return result != null && result;
        } catch (Exception e) {
            return false;
        }
    }
}