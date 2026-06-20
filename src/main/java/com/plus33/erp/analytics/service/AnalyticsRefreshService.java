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
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void refreshViewWithNewTransaction(String viewName) {
        try {
            if (isPopulated(viewName)) {
                jdbcTemplate.execute("REFRESH MATERIALIZED VIEW CONCURRENTLY " + viewName);
            } else {
                jdbcTemplate.execute("REFRESH MATERIALIZED VIEW " + viewName);
            }
        } catch (Exception e) {
            // Log the error but do not propagate it, so other views are not blocked
            System.err.println("Failed to refresh materialized view " + viewName + ": " + e.getMessage());
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
