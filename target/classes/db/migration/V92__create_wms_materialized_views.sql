-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 92
-- File              : V92__create_wms_materialized_views.sql
-- Operation Type    : DDL
-- Purpose           : create wms materialized views
--
-- Tables Created    : N/A
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : IF, IF, IF, IF, IF, IF, IF, IF
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V92__create_wms_materialized_views.sql
-- PLUS33 ERP — WMS Analytics Materialized Views
-- ============================================================

-- Inventory Accuracy: % of bins with zero variance after last cycle count
CREATE MATERIALIZED VIEW IF NOT EXISTS mv_inventory_accuracy AS
SELECT
    ccr.company_id,
    wl.warehouse_id,
    COUNT(*) AS total_counts,
    COUNT(*) FILTER (WHERE ccr.variance_quantity = 0) AS accurate_counts,
    ROUND(
        COUNT(*) FILTER (WHERE ccr.variance_quantity = 0) * 100.0 / NULLIF(COUNT(*), 0), 2
    ) AS accuracy_pct,
    SUM(ABS(ccr.variance_value)) AS total_variance_value,
    NOW() AS refreshed_at
FROM cycle_count_results ccr
JOIN warehouse_locations wl ON wl.id = ccr.location_id
WHERE ccr.status IN ('APPROVED', 'AUTO_APPROVED')
GROUP BY ccr.company_id, wl.warehouse_id;

CREATE UNIQUE INDEX IF NOT EXISTS idx_mv_inv_accuracy ON mv_inventory_accuracy (company_id, warehouse_id);

-- Pick Rate: picks per hour by warehouse
CREATE MATERIALIZED VIEW IF NOT EXISTS mv_pick_rate AS
SELECT
    pw.company_id,
    w.warehouse_id,
    DATE_TRUNC('day', pw.completed_at) AS pick_date,
    COUNT(*) AS total_picks,
    SUM(pw.picked_quantity) AS total_quantity_picked,
    ROUND(
        COUNT(*) / NULLIF(
            EXTRACT(EPOCH FROM (MAX(pw.completed_at) - MIN(pw.started_at))) / 3600, 0
        ), 2
    ) AS picks_per_hour,
    NOW() AS refreshed_at
FROM picking_work pw
JOIN waves w ON w.id = pw.wave_id
WHERE pw.status = 'COMPLETED'
  AND pw.completed_at IS NOT NULL
GROUP BY pw.company_id, w.warehouse_id, DATE_TRUNC('day', pw.completed_at);

CREATE UNIQUE INDEX IF NOT EXISTS idx_mv_pick_rate ON mv_pick_rate (company_id, warehouse_id, pick_date);

-- Put-Away Rate: tasks per hour by warehouse
CREATE MATERIALIZED VIEW IF NOT EXISTS mv_putaway_rate AS
SELECT
    paw.company_id,
    paw.warehouse_id,
    DATE_TRUNC('day', paw.completed_at) AS putaway_date,
    COUNT(*) AS total_tasks,
    SUM(paw.quantity) AS total_quantity,
    ROUND(
        COUNT(*) / NULLIF(
            EXTRACT(EPOCH FROM (MAX(paw.completed_at) - MIN(paw.started_at))) / 3600, 0
        ), 2
    ) AS tasks_per_hour,
    NOW() AS refreshed_at
FROM put_away_work paw
WHERE paw.status = 'COMPLETED'
  AND paw.completed_at IS NOT NULL
GROUP BY paw.company_id, paw.warehouse_id, DATE_TRUNC('day', paw.completed_at);

CREATE UNIQUE INDEX IF NOT EXISTS idx_mv_putaway_rate ON mv_putaway_rate (company_id, warehouse_id, putaway_date);

-- Dock Utilization: dock door busy time vs available time
CREATE MATERIALIZED VIEW IF NOT EXISTS mv_dock_utilization AS
SELECT
    ds.company_id,
    dd.warehouse_id,
    DATE_TRUNC('day', ds.scheduled_start) AS schedule_date,
    COUNT(DISTINCT ds.dock_door_id) AS doors_used,
    COUNT(*) AS total_bookings,
    SUM(
        EXTRACT(EPOCH FROM (COALESCE(ds.actual_end, ds.scheduled_end) - COALESCE(ds.actual_start, ds.scheduled_start))) / 3600
    ) AS total_dock_hours,
    NOW() AS refreshed_at
FROM dock_schedules ds
JOIN dock_doors dd ON dd.id = ds.dock_door_id
GROUP BY ds.company_id, dd.warehouse_id, DATE_TRUNC('day', ds.scheduled_start);

CREATE UNIQUE INDEX IF NOT EXISTS idx_mv_dock_util ON mv_dock_utilization (company_id, warehouse_id, schedule_date);

-- Space Utilization: quantity vs capacity per zone
CREATE MATERIALIZED VIEW IF NOT EXISTS mv_space_utilization AS
SELECT
    ls.company_id,
    wl.warehouse_id,
    wl.zone_id,
    wz.name AS zone_name,
    COUNT(DISTINCT wl.id) AS total_locations,
    COUNT(DISTINCT ls.location_id) AS occupied_locations,
    ROUND(COUNT(DISTINCT ls.location_id) * 100.0 / NULLIF(COUNT(DISTINCT wl.id), 0), 2) AS occupancy_pct,
    NOW() AS refreshed_at
FROM warehouse_locations wl
JOIN warehouse_zones wz ON wz.id = wl.zone_id
LEFT JOIN location_stock ls ON ls.location_id = wl.id AND ls.quantity > 0
WHERE wl.active = TRUE
GROUP BY ls.company_id, wl.warehouse_id, wl.zone_id, wz.name;

CREATE UNIQUE INDEX IF NOT EXISTS idx_mv_space_util ON mv_space_utilization (company_id, warehouse_id, zone_id);

-- Order Fulfillment: shipped vs ordered quantity
CREATE MATERIALIZED VIEW IF NOT EXISTS mv_order_fulfillment AS
SELECT
    s.company_id,
    s.warehouse_id,
    DATE_TRUNC('month', s.created_at) AS period,
    COUNT(DISTINCT s.id) AS total_shipments,
    COUNT(DISTINCT s.id) FILTER (WHERE s.status = 'DELIVERED') AS delivered,
    ROUND(
        COUNT(DISTINCT s.id) FILTER (WHERE s.status = 'DELIVERED') * 100.0 / NULLIF(COUNT(DISTINCT s.id), 0), 2
    ) AS fulfillment_rate_pct,
    NOW() AS refreshed_at
FROM shipments s
GROUP BY s.company_id, s.warehouse_id, DATE_TRUNC('month', s.created_at);

CREATE UNIQUE INDEX IF NOT EXISTS idx_mv_order_fulfillment ON mv_order_fulfillment (company_id, warehouse_id, period);

-- Cycle Count Variance: summary by month and warehouse
CREATE MATERIALIZED VIEW IF NOT EXISTS mv_cycle_count_variance AS
SELECT
    ccr.company_id,
    wl.warehouse_id,
    DATE_TRUNC('month', ccr.counted_at) AS period,
    COUNT(*) AS total_counts,
    COUNT(*) FILTER (WHERE ccr.variance_quantity != 0) AS counts_with_variance,
    SUM(ABS(ccr.variance_value)) AS total_variance_value,
    ROUND(AVG(ABS(ccr.variance_pct)), 4) AS avg_variance_pct,
    NOW() AS refreshed_at
FROM cycle_count_results ccr
JOIN warehouse_locations wl ON wl.id = ccr.location_id
WHERE ccr.status IN ('APPROVED', 'AUTO_APPROVED')
GROUP BY ccr.company_id, wl.warehouse_id, DATE_TRUNC('month', ccr.counted_at);

CREATE UNIQUE INDEX IF NOT EXISTS idx_mv_cycle_variance ON mv_cycle_count_variance (company_id, warehouse_id, period);

-- Inventory Turnover: movement volume relative to average stock
CREATE MATERIALIZED VIEW IF NOT EXISTS mv_wms_inventory_turnover AS
SELECT
    im.company_id,
    im.warehouse_id,
    im.product_id,
    DATE_TRUNC('month', im.movement_at) AS period,
    SUM(im.quantity) FILTER (WHERE im.movement_type IN ('ISSUE', 'PICK', 'TRANSFER_OUT', 'CROSS_DOCK')) AS units_consumed,
    SUM(im.total_cost) FILTER (WHERE im.movement_type IN ('ISSUE', 'PICK', 'TRANSFER_OUT', 'CROSS_DOCK')) AS cost_consumed,
    NOW() AS refreshed_at
FROM inventory_movements im
GROUP BY im.company_id, im.warehouse_id, im.product_id, DATE_TRUNC('month', im.movement_at);

CREATE UNIQUE INDEX IF NOT EXISTS idx_mv_wms_inventory_turnover ON mv_wms_inventory_turnover (company_id, warehouse_id, product_id, period);
