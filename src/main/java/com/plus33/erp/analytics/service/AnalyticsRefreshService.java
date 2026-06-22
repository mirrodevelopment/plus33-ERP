package com.plus33.erp.analytics.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Scheduled(cron = "0 0,15,30,45 * * * *")
    public void refreshKpis() {
        refreshViewWithNewTransaction("mv_inventory_kpis");
    }

    @Scheduled(cron = "0 5,20,35,50 * * * *")
    public void refreshAging() {
        refreshViewWithNewTransaction("mv_inventory_aging_expiry");
    }

    @Scheduled(cron = "0 10,25,40,55 * * * *")
    public void refreshAbcXyz() {
        refreshDemandSummary();
        refreshViewWithNewTransaction("mv_inventory_abc_xyz");
    }

    @Scheduled(cron = "0 12,27,42,57 * * * *")
    public void refreshTraceability() {
        refreshViewWithNewTransaction("mv_inventory_traceability_metrics");
    }

    @Scheduled(cron = "0 14,29,44,59 * * * *")
    public void refreshReplenishment() {
        refreshViewWithNewTransaction("mv_inventory_replenishment_metrics");
    }

    @Scheduled(cron = "0 7,22,37,52 * * * *")
    public void refreshTurnover() {
        refreshViewWithNewTransaction("mv_inventory_turnover");
    }

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
