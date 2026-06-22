package com.plus33.erp.analytics.service;

import com.plus33.erp.analytics.dto.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class InventoryAnalyticsServiceImpl implements InventoryAnalyticsService {

    private final JdbcTemplate jdbcTemplate;
    private final AnalyticsRefreshService analyticsRefreshService;

    public InventoryAnalyticsServiceImpl(JdbcTemplate jdbcTemplate, AnalyticsRefreshService analyticsRefreshService) {
        this.jdbcTemplate = jdbcTemplate;
        this.analyticsRefreshService = analyticsRefreshService;
    }

    @Override
    public InventoryDashboardResponse getDashboard(Long companyId, Long warehouseId, Long storeId) {
        InventoryKpisResponse kpis = getKpis(companyId, warehouseId, storeId);
        InventoryAgingExpiryResponse aging = getAgingExpiry(companyId, warehouseId, storeId);
        InventoryReplenishmentResponse repl = getReplenishmentMetrics(companyId, warehouseId, storeId);
        InventoryTraceabilityResponse trace = getTraceabilityMetrics(companyId, warehouseId, storeId);
        InventoryTurnoverResponse turnover = getTurnover(companyId, warehouseId, storeId);
        return new InventoryDashboardResponse(kpis, aging, repl, trace, turnover);
    }

    @Override
    public InventoryKpisResponse getKpis(Long companyId, Long warehouseId, Long storeId) {
        return querySingle(
            "mv_inventory_kpis",
            "company_id, warehouse_id, store_id, total_stock_value, total_unique_products, out_of_stock_products, low_stock_products, overstock_products",
            companyId, warehouseId, storeId,
            (rs, rowNum) -> new InventoryKpisResponse(
                rs.getLong("company_id"),
                rs.getObject("warehouse_id") != null ? rs.getLong("warehouse_id") : null,
                rs.getObject("store_id") != null ? rs.getLong("store_id") : null,
                rs.getBigDecimal("total_stock_value"),
                rs.getLong("total_unique_products"),
                rs.getLong("out_of_stock_products"),
                rs.getLong("low_stock_products"),
                rs.getLong("overstock_products")
            ),
            new InventoryKpisResponse(companyId, warehouseId, storeId, java.math.BigDecimal.ZERO, 0L, 0L, 0L, 0L)
        );
    }

    @Override
    public InventoryAgingExpiryResponse getAgingExpiry(Long companyId, Long warehouseId, Long storeId) {
        return querySingle(
            "mv_inventory_aging_expiry",
            "company_id, warehouse_id, store_id, aging_0_30, aging_31_90, aging_91_180, aging_180_plus, expired_lots_count, expiring_0_30_count, expiring_31_90_count, expiring_91_180_count, safe_lots_count",
            companyId, warehouseId, storeId,
            (rs, rowNum) -> new InventoryAgingExpiryResponse(
                rs.getLong("company_id"),
                rs.getObject("warehouse_id") != null ? rs.getLong("warehouse_id") : null,
                rs.getObject("store_id") != null ? rs.getLong("store_id") : null,
                rs.getBigDecimal("aging_0_30"),
                rs.getBigDecimal("aging_31_90"),
                rs.getBigDecimal("aging_91_180"),
                rs.getBigDecimal("aging_180_plus"),
                rs.getLong("expired_lots_count"),
                rs.getLong("expiring_0_30_count"),
                rs.getLong("expiring_31_90_count"),
                rs.getLong("expiring_91_180_count"),
                rs.getLong("safe_lots_count")
            ),
            new InventoryAgingExpiryResponse(companyId, warehouseId, storeId, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, 0L, 0L, 0L, 0L, 0L)
        );
    }

    @Override
    public List<InventoryAbcXyzResponse> getAbcXyz(Long companyId, Long warehouseId, Long storeId) {
        String sql;
        Object[] args;
        if (warehouseId != null) {
            sql = "SELECT company_id, product_id, warehouse_id, store_id, abc_class, xyz_class FROM mv_inventory_abc_xyz WHERE company_id = ? AND warehouse_id = ? AND store_id IS NULL";
            args = new Object[]{companyId, warehouseId};
        } else if (storeId != null) {
            sql = "SELECT company_id, product_id, warehouse_id, store_id, abc_class, xyz_class FROM mv_inventory_abc_xyz WHERE company_id = ? AND store_id = ? AND warehouse_id IS NULL";
            args = new Object[]{companyId, storeId};
        } else {
            sql = "SELECT company_id, product_id, warehouse_id, store_id, abc_class, xyz_class FROM mv_inventory_abc_xyz WHERE company_id = ? AND warehouse_id IS NULL AND store_id IS NULL";
            args = new Object[]{companyId};
        }
        return jdbcTemplate.query(sql, (rs, rowNum) -> new InventoryAbcXyzResponse(
            rs.getLong("company_id"),
            rs.getLong("product_id"),
            rs.getObject("warehouse_id") != null ? rs.getLong("warehouse_id") : null,
            rs.getObject("store_id") != null ? rs.getLong("store_id") : null,
            rs.getString("abc_class"),
            rs.getString("xyz_class")
        ), args);
    }

    @Override
    public List<InventorySlowDeadResponse> getSlowDead(Long companyId, Long warehouseId, Long storeId) {
        String sql;
        Object[] args;
        if (warehouseId != null) {
            sql = "SELECT company_id, product_id, warehouse_id, store_id, on_hand_quantity, last_movement_at, usage_90_days, is_dead, is_slow FROM mv_inventory_slow_dead WHERE company_id = ? AND warehouse_id = ? AND store_id IS NULL";
            args = new Object[]{companyId, warehouseId};
        } else if (storeId != null) {
            sql = "SELECT company_id, product_id, warehouse_id, store_id, on_hand_quantity, last_movement_at, usage_90_days, is_dead, is_slow FROM mv_inventory_slow_dead WHERE company_id = ? AND store_id = ? AND warehouse_id IS NULL";
            args = new Object[]{companyId};
        } else {
            sql = "SELECT company_id, product_id, warehouse_id, store_id, on_hand_quantity, last_movement_at, usage_90_days, is_dead, is_slow FROM mv_inventory_slow_dead WHERE company_id = ? AND warehouse_id IS NULL AND store_id IS NULL";
            args = new Object[]{companyId};
        }
        return jdbcTemplate.query(sql, (rs, rowNum) -> new InventorySlowDeadResponse(
            rs.getLong("company_id"),
            rs.getLong("product_id"),
            rs.getObject("warehouse_id") != null ? rs.getLong("warehouse_id") : null,
            rs.getObject("store_id") != null ? rs.getLong("store_id") : null,
            rs.getBigDecimal("on_hand_quantity"),
            rs.getTimestamp("last_movement_at") != null ? rs.getTimestamp("last_movement_at").toLocalDateTime() : null,
            rs.getBigDecimal("usage_90_days"),
            rs.getBoolean("is_dead"),
            rs.getBoolean("is_slow")
        ), args);
    }

    @Override
    public InventoryTurnoverResponse getTurnover(Long companyId, Long warehouseId, Long storeId) {
        return querySingle(
            "mv_inventory_turnover",
            "company_id, warehouse_id, store_id, cost_of_goods_sold, average_inventory_value, inventory_turnover_ratio, days_on_hand",
            companyId, warehouseId, storeId,
            (rs, rowNum) -> new InventoryTurnoverResponse(
                rs.getLong("company_id"),
                rs.getObject("warehouse_id") != null ? rs.getLong("warehouse_id") : null,
                rs.getObject("store_id") != null ? rs.getLong("store_id") : null,
                rs.getBigDecimal("cost_of_goods_sold"),
                rs.getBigDecimal("average_inventory_value"),
                rs.getBigDecimal("inventory_turnover_ratio"),
                rs.getBigDecimal("days_on_hand")
            ),
            new InventoryTurnoverResponse(companyId, warehouseId, storeId, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO)
        );
    }

    @Override
    public InventoryReplenishmentResponse getReplenishmentMetrics(Long companyId, Long warehouseId, Long storeId) {
        return querySingle(
            "mv_inventory_replenishment_metrics",
            "company_id, warehouse_id, store_id, rules_count, rules_coverage_percent, avg_replenishment_cycle_time_hours, stockout_prevention_rate",
            companyId, warehouseId, storeId,
            (rs, rowNum) -> new InventoryReplenishmentResponse(
                rs.getLong("company_id"),
                rs.getObject("warehouse_id") != null ? rs.getLong("warehouse_id") : null,
                rs.getObject("store_id") != null ? rs.getLong("store_id") : null,
                rs.getLong("rules_count"),
                rs.getBigDecimal("rules_coverage_percent"),
                rs.getBigDecimal("avg_replenishment_cycle_time_hours"),
                rs.getBigDecimal("stockout_prevention_rate")
            ),
            new InventoryReplenishmentResponse(companyId, warehouseId, storeId, 0L, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO)
        );
    }

    @Override
    public InventoryTraceabilityResponse getTraceabilityMetrics(Long companyId, Long warehouseId, Long storeId) {
        return querySingle(
            "mv_inventory_traceability_metrics",
            "company_id, warehouse_id, store_id, active_recalls_count, recalled_lots_count, recalled_serials_count, total_recalled_quantity, compromise_rate",
            companyId, warehouseId, storeId,
            (rs, rowNum) -> new InventoryTraceabilityResponse(
                rs.getLong("company_id"),
                rs.getObject("warehouse_id") != null ? rs.getLong("warehouse_id") : null,
                rs.getObject("store_id") != null ? rs.getObject("store_id") != null ? rs.getLong("store_id") : null : null,
                rs.getLong("active_recalls_count"),
                rs.getLong("recalled_lots_count"),
                rs.getLong("recalled_serials_count"),
                rs.getBigDecimal("total_recalled_quantity"),
                rs.getBigDecimal("compromise_rate")
            ),
            new InventoryTraceabilityResponse(companyId, warehouseId, storeId, 0L, 0L, 0L, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO)
        );
    }

    @Override
    public List<AnalyticsHealthResponse> getHealth() {
        String sql = "SELECT view_name, last_refreshed_at, refresh_duration_ms, refresh_status FROM analytics_refresh_log ORDER BY view_name";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new AnalyticsHealthResponse(
            rs.getString("view_name"),
            rs.getTimestamp("last_refreshed_at").toLocalDateTime(),
            rs.getLong("refresh_duration_ms"),
            rs.getString("refresh_status")
        ));
    }

    @Override
    @Transactional
    public void refreshAllViews() {
        analyticsRefreshService.refreshDemandSummary();
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_kpis");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_aging_expiry");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_abc_xyz");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_slow_dead");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_replenishment_metrics");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_traceability_metrics");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_turnover");
    }

    private <T> T querySingle(String viewName, String selectColumns, Long companyId, Long warehouseId, Long storeId, RowMapper<T> mapper, T defaultValue) {
        try {
            String sql;
            Object[] args;
            if (warehouseId != null) {
                sql = "SELECT " + selectColumns + " FROM " + viewName + " WHERE company_id = ? AND warehouse_id = ? AND store_id IS NULL";
                args = new Object[]{companyId, warehouseId};
            } else if (storeId != null) {
                sql = "SELECT " + selectColumns + " FROM " + viewName + " WHERE company_id = ? AND store_id = ? AND warehouse_id IS NULL";
                args = new Object[]{companyId, storeId};
            } else {
                sql = "SELECT " + selectColumns + " FROM " + viewName + " WHERE company_id = ? AND warehouse_id IS NULL AND store_id IS NULL";
                args = new Object[]{companyId};
            }
            return jdbcTemplate.queryForObject(sql, mapper, args);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
