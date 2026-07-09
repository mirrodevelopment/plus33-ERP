-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 50
-- File              : V50__create_inventory_analytics.sql
-- Operation Type    : Schema Creation
-- Purpose           : create inventory analytics
--
-- Tables Created    : weekly_product_demand_summary, analytics_refresh_log
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : uidx_weekly_product_demand_summary, uidx_mv_inventory_kpis, uidx_mv_inventory_aging_expiry, uidx_mv_inventory_abc_xyz, uidx_mv_inventory_slow_dead, uidx_mv_inventory_repl_metrics, uidx_mv_inventory_trace_metrics, uidx_mv_inventory_turnover
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V50__create_inventory_analytics.sql
-- PLUS33 ERP — Inventory Analytics & Dashboards Materialized Views
-- ============================================================

-- 1. Demand summary table for performance optimization
CREATE TABLE weekly_product_demand_summary (
    id                 BIGSERIAL PRIMARY KEY,
    company_id         BIGINT NOT NULL REFERENCES companies(id),
    product_id         BIGINT NOT NULL REFERENCES products(id),
    warehouse_id       BIGINT REFERENCES warehouses(id),
    store_id           BIGINT REFERENCES stores(id),
    week_start_date    DATE NOT NULL,
    total_quantity     NUMERIC(12,2) NOT NULL
);

CREATE UNIQUE INDEX uidx_weekly_product_demand_summary ON weekly_product_demand_summary (
    company_id,
    product_id,
    COALESCE(warehouse_id, 0),
    COALESCE(store_id, 0),
    week_start_date
);

-- 2. Health Monitoring Log Table
CREATE TABLE analytics_refresh_log (
    view_name           VARCHAR(100) PRIMARY KEY,
    last_refreshed_at   TIMESTAMP NOT NULL,
    refresh_duration_ms BIGINT NOT NULL,
    refresh_status      VARCHAR(20) NOT NULL
);

-- Helper avg purchase price CTE query structure
-- View 1: mv_inventory_kpis
CREATE MATERIALIZED VIEW mv_inventory_kpis AS
WITH product_avg_price AS (
    SELECT product_id, COALESCE(AVG(unit_price), 0.00) AS avg_price
    FROM purchase_order_items
    GROUP BY product_id
),
warehouse_stock AS (
    SELECT 
        w.region_id,
        r.company_id,
        s.warehouse_id,
        s.product_id,
        s.quantity,
        CASE WHEN s.quantity <= COALESCE(rule.min_quantity, p.reorder_level, 0.00) THEN 1 ELSE 0 END AS is_low_stock,
        CASE WHEN rule.max_quantity IS NOT NULL AND s.quantity > rule.max_quantity THEN 1 ELSE 0 END AS is_overstock
    FROM inventory_stock s
    JOIN products p ON s.product_id = p.id AND p.active = TRUE
    JOIN warehouses w ON s.warehouse_id = w.id
    JOIN regions r ON w.region_id = r.id
    LEFT JOIN replenishment_rules rule ON rule.product_id = s.product_id AND rule.warehouse_id = s.warehouse_id AND rule.active = TRUE
),
store_stock AS (
    SELECT 
        st.region_id,
        r.company_id,
        s.store_id,
        s.product_id,
        s.quantity,
        CASE WHEN s.quantity <= COALESCE(rule.min_quantity, p.reorder_level, 0.00) THEN 1 ELSE 0 END AS is_low_stock,
        CASE WHEN rule.max_quantity IS NOT NULL AND s.quantity > rule.max_quantity THEN 1 ELSE 0 END AS is_overstock
    FROM inventory_stock s
    JOIN products p ON s.product_id = p.id AND p.active = TRUE
    JOIN stores st ON s.store_id = st.id
    JOIN regions r ON st.region_id = r.id
    LEFT JOIN replenishment_rules rule ON rule.product_id = s.product_id AND rule.store_id = s.store_id AND rule.active = TRUE
),
location_kpi AS (
    SELECT 
        company_id,
        warehouse_id,
        NULL::BIGINT AS store_id,
        SUM(quantity * COALESCE(ap.avg_price, 0.00)) AS total_stock_value,
        COUNT(DISTINCT ws.product_id) AS total_unique_products,
        COUNT(DISTINCT CASE WHEN quantity = 0 THEN ws.product_id END) AS out_of_stock_products,
        SUM(is_low_stock) AS low_stock_products,
        SUM(is_overstock) AS overstock_products
    FROM warehouse_stock ws
    LEFT JOIN product_avg_price ap ON ap.product_id = ws.product_id
    GROUP BY company_id, warehouse_id
    
    UNION ALL
    
    SELECT 
        company_id,
        NULL::BIGINT AS warehouse_id,
        store_id,
        SUM(quantity * COALESCE(ap.avg_price, 0.00)) AS total_stock_value,
        COUNT(DISTINCT ss.product_id) AS total_unique_products,
        COUNT(DISTINCT CASE WHEN quantity = 0 THEN ss.product_id END) AS out_of_stock_products,
        SUM(is_low_stock) AS low_stock_products,
        SUM(is_overstock) AS overstock_products
    FROM store_stock ss
    LEFT JOIN product_avg_price ap ON ap.product_id = ss.product_id
    GROUP BY company_id, store_id
),
company_stock AS (
    SELECT 
        company_id,
        product_id,
        SUM(quantity) AS quantity,
        CASE WHEN SUM(quantity) <= COALESCE(MAX(p.reorder_level), 0.00) THEN 1 ELSE 0 END AS is_low_stock
    FROM (
        SELECT company_id, product_id, quantity FROM warehouse_stock
        UNION ALL
        SELECT company_id, product_id, quantity FROM store_stock
    ) t
    JOIN products p ON t.product_id = p.id
    GROUP BY company_id, product_id
),
company_kpi AS (
    SELECT 
        company_id,
        NULL::BIGINT AS warehouse_id,
        NULL::BIGINT AS store_id,
        SUM(quantity * COALESCE(ap.avg_price, 0.00)) AS total_stock_value,
        COUNT(DISTINCT cs.product_id) AS total_unique_products,
        COUNT(DISTINCT CASE WHEN quantity = 0 THEN cs.product_id END) AS out_of_stock_products,
        SUM(is_low_stock) AS low_stock_products,
        0::BIGINT AS overstock_products
    FROM company_stock cs
    LEFT JOIN product_avg_price ap ON ap.product_id = cs.product_id
    GROUP BY company_id
)
SELECT 
    c.id AS company_id,
    l.warehouse_id,
    l.store_id,
    COALESCE(l.total_stock_value, 0.00) AS total_stock_value,
    COALESCE(l.total_unique_products, 0) AS total_unique_products,
    COALESCE(l.out_of_stock_products, 0) AS out_of_stock_products,
    COALESCE(l.low_stock_products, 0) AS low_stock_products,
    COALESCE(l.overstock_products, 0) AS overstock_products
FROM companies c
JOIN (
    SELECT company_id, warehouse_id, store_id, total_stock_value, total_unique_products, out_of_stock_products, low_stock_products, overstock_products FROM location_kpi
    UNION ALL
    SELECT company_id, warehouse_id, store_id, total_stock_value, total_unique_products, out_of_stock_products, low_stock_products, overstock_products FROM company_kpi
) l ON c.id = l.company_id
WITH NO DATA;


-- View 2: mv_inventory_aging_expiry
CREATE MATERIALIZED VIEW mv_inventory_aging_expiry AS
WITH current_stock AS (
    SELECT 
        s.product_id,
        s.warehouse_id,
        s.store_id,
        s.quantity,
        COALESCE(w.region_id, st.region_id) AS region_id,
        r.company_id
    FROM inventory_stock s
    LEFT JOIN warehouses w ON s.warehouse_id = w.id
    LEFT JOIN stores st ON s.store_id = st.id
    JOIN regions r ON r.id = COALESCE(w.region_id, st.region_id)
),
latest_movements AS (
    SELECT 
        product_id,
        warehouse_id,
        store_id,
        MIN(EXTRACT(DAY FROM (NOW() - movement_at))) AS min_age_days
    FROM stock_movements
    WHERE quantity > 0
    GROUP BY product_id, warehouse_id, store_id
),
lots_risk AS (
    SELECT 
        l.company_id,
        l.product_id,
        s.warehouse_id,
        s.store_id,
        COUNT(CASE WHEN l.expiry_date < CURRENT_DATE THEN 1 END) AS expired_lots,
        COUNT(CASE WHEN l.expiry_date >= CURRENT_DATE AND l.expiry_date <= CURRENT_DATE + 30 THEN 1 END) AS expiring_0_30,
        COUNT(CASE WHEN l.expiry_date > CURRENT_DATE + 30 AND l.expiry_date <= CURRENT_DATE + 90 THEN 1 END) AS expiring_31_90,
        COUNT(CASE WHEN l.expiry_date > CURRENT_DATE + 90 AND l.expiry_date <= CURRENT_DATE + 180 THEN 1 END) AS expiring_91_180,
        COUNT(CASE WHEN l.expiry_date > CURRENT_DATE + 180 THEN 1 END) AS safe_lots
    FROM inventory_lots l
    LEFT JOIN inventory_serials s ON s.lot_id = l.id
    GROUP BY l.company_id, l.product_id, s.warehouse_id, s.store_id
),
location_aging AS (
    SELECT 
        cs.company_id,
        cs.warehouse_id,
        cs.store_id,
        SUM(CASE WHEN COALESCE(lm.min_age_days, 0) <= 30 THEN cs.quantity ELSE 0.00 END) AS aging_0_30,
        SUM(CASE WHEN COALESCE(lm.min_age_days, 0) > 30 AND COALESCE(lm.min_age_days, 0) <= 90 THEN cs.quantity ELSE 0.00 END) AS aging_31_90,
        SUM(CASE WHEN COALESCE(lm.min_age_days, 0) > 90 AND COALESCE(lm.min_age_days, 0) <= 180 THEN cs.quantity ELSE 0.00 END) AS aging_91_180,
        SUM(CASE WHEN COALESCE(lm.min_age_days, 0) > 180 THEN cs.quantity ELSE 0.00 END) AS aging_180_plus,
        COALESCE(SUM(lr.expired_lots), 0) AS expired_lots_count,
        COALESCE(SUM(lr.expiring_0_30), 0) AS expiring_0_30_count,
        COALESCE(SUM(lr.expiring_31_90), 0) AS expiring_31_90_count,
        COALESCE(SUM(lr.expiring_91_180), 0) AS expiring_91_180_count,
        COALESCE(SUM(lr.safe_lots), 0) AS safe_lots_count
    FROM current_stock cs
    LEFT JOIN latest_movements lm ON lm.product_id = cs.product_id AND COALESCE(lm.warehouse_id, 0) = COALESCE(cs.warehouse_id, 0) AND COALESCE(lm.store_id, 0) = COALESCE(cs.store_id, 0)
    LEFT JOIN lots_risk lr ON lr.product_id = cs.product_id AND COALESCE(lr.warehouse_id, 0) = COALESCE(cs.warehouse_id, 0) AND COALESCE(lr.store_id, 0) = COALESCE(cs.store_id, 0)
    GROUP BY cs.company_id, cs.warehouse_id, cs.store_id
),
company_aging AS (
    SELECT 
        cs.company_id,
        NULL::BIGINT AS warehouse_id,
        NULL::BIGINT AS store_id,
        SUM(CASE WHEN COALESCE(lm.min_age_days, 0) <= 30 THEN cs.quantity ELSE 0.00 END) AS aging_0_30,
        SUM(CASE WHEN COALESCE(lm.min_age_days, 0) > 30 AND COALESCE(lm.min_age_days, 0) <= 90 THEN cs.quantity ELSE 0.00 END) AS aging_31_90,
        SUM(CASE WHEN COALESCE(lm.min_age_days, 0) > 90 AND COALESCE(lm.min_age_days, 0) <= 180 THEN cs.quantity ELSE 0.00 END) AS aging_91_180,
        SUM(CASE WHEN COALESCE(lm.min_age_days, 0) > 180 THEN cs.quantity ELSE 0.00 END) AS aging_180_plus,
        COALESCE(SUM(lr.expired_lots), 0) AS expired_lots_count,
        COALESCE(SUM(lr.expiring_0_30), 0) AS expiring_0_30_count,
        COALESCE(SUM(lr.expiring_31_90), 0) AS expiring_31_90_count,
        COALESCE(SUM(lr.expiring_91_180), 0) AS expiring_91_180_count,
        COALESCE(SUM(lr.safe_lots), 0) AS safe_lots_count
    FROM current_stock cs
    LEFT JOIN latest_movements lm ON lm.product_id = cs.product_id AND COALESCE(lm.warehouse_id, 0) = COALESCE(cs.warehouse_id, 0) AND COALESCE(lm.store_id, 0) = COALESCE(cs.store_id, 0)
    LEFT JOIN lots_risk lr ON lr.product_id = cs.product_id AND COALESCE(lr.warehouse_id, 0) = COALESCE(cs.warehouse_id, 0) AND COALESCE(lr.store_id, 0) = COALESCE(cs.store_id, 0)
    GROUP BY cs.company_id
)
SELECT * FROM location_aging
UNION ALL
SELECT * FROM company_aging
WITH NO DATA;


-- View 3: mv_inventory_abc_xyz
CREATE MATERIALIZED VIEW mv_inventory_abc_xyz AS
WITH active_product_locations AS (
    SELECT DISTINCT 
        r.company_id,
        p.id AS product_id,
        inv.warehouse_id,
        inv.store_id
    FROM products p
    JOIN inventory_stock inv ON inv.product_id = p.id
    LEFT JOIN warehouses w ON inv.warehouse_id = w.id
    LEFT JOIN stores s ON inv.store_id = s.id
    JOIN regions r ON r.id = COALESCE(w.region_id, s.region_id)
    WHERE p.active = TRUE
    
    UNION DISTINCT
    
    SELECT DISTINCT
        r.company_id,
        p.id AS product_id,
        NULL::BIGINT AS warehouse_id,
        NULL::BIGINT AS store_id
    FROM products p
    JOIN inventory_stock inv ON inv.product_id = p.id
    LEFT JOIN warehouses w ON inv.warehouse_id = w.id
    LEFT JOIN stores s ON inv.store_id = s.id
    JOIN regions r ON r.id = COALESCE(w.region_id, s.region_id)
    WHERE p.active = TRUE
),
product_avg_price AS (
    SELECT product_id, COALESCE(AVG(unit_price), 0.00) AS avg_price
    FROM purchase_order_items
    GROUP BY product_id
),
consumption_value AS (
    SELECT 
        wds.company_id,
        wds.product_id,
        wds.warehouse_id,
        wds.store_id,
        SUM(wds.total_quantity * COALESCE(ap.avg_price, 0.00)) AS total_value
    FROM weekly_product_demand_summary wds
    LEFT JOIN product_avg_price ap ON ap.product_id = wds.product_id
    GROUP BY wds.company_id, wds.product_id, wds.warehouse_id, wds.store_id
),
abc_ranked AS (
    SELECT 
        company_id,
        product_id,
        warehouse_id,
        store_id,
        total_value,
        PERCENT_RANK() OVER (PARTITION BY company_id, COALESCE(warehouse_id, 0), COALESCE(store_id, 0) ORDER BY total_value DESC) AS val_rank
    FROM consumption_value
),
xyz_stats AS (
    SELECT 
        company_id,
        product_id,
        warehouse_id,
        store_id,
        STDDEV(total_quantity) AS stddev_qty,
        AVG(total_quantity) AS avg_qty
    FROM weekly_product_demand_summary
    GROUP BY company_id, product_id, warehouse_id, store_id
),
xyz_calc AS (
    SELECT 
        company_id,
        product_id,
        warehouse_id,
        store_id,
        CASE 
            WHEN avg_qty IS NULL OR avg_qty = 0 THEN 999.00
            ELSE stddev_qty / avg_qty 
        END AS cv
    FROM xyz_stats
)
SELECT 
    apl.company_id,
    apl.product_id,
    apl.warehouse_id,
    apl.store_id,
    CASE 
        WHEN abc.val_rank IS NULL THEN 'C'
        WHEN abc.val_rank <= 0.20 THEN 'A'
        WHEN abc.val_rank <= 0.50 THEN 'B'
        ELSE 'C'
    END AS abc_class,
    CASE 
        WHEN xyz.cv IS NULL THEN 'Z'
        WHEN xyz.cv <= 0.20 THEN 'X'
        WHEN xyz.cv <= 0.50 THEN 'Y'
        ELSE 'Z'
    END AS xyz_class
FROM active_product_locations apl
LEFT JOIN abc_ranked abc ON abc.company_id = apl.company_id 
    AND abc.product_id = apl.product_id 
    AND COALESCE(abc.warehouse_id, 0) = COALESCE(apl.warehouse_id, 0) 
    AND COALESCE(abc.store_id, 0) = COALESCE(apl.store_id, 0)
LEFT JOIN xyz_calc xyz ON xyz.company_id = apl.company_id 
    AND xyz.product_id = apl.product_id 
    AND COALESCE(xyz.warehouse_id, 0) = COALESCE(apl.warehouse_id, 0) 
    AND COALESCE(xyz.store_id, 0) = COALESCE(apl.store_id, 0)
WITH NO DATA;


-- View 4: mv_inventory_slow_dead
CREATE MATERIALIZED VIEW mv_inventory_slow_dead AS
WITH current_stock AS (
    SELECT 
        s.product_id,
        s.warehouse_id,
        s.store_id,
        s.quantity,
        r.company_id
    FROM inventory_stock s
    LEFT JOIN warehouses w ON s.warehouse_id = w.id
    LEFT JOIN stores st ON s.store_id = st.id
    JOIN regions r ON r.id = COALESCE(w.region_id, st.region_id)
    WHERE s.quantity > 0
),
latest_activity AS (
    SELECT 
        product_id,
        warehouse_id,
        store_id,
        MAX(movement_at) AS last_movement_at,
        SUM(CASE WHEN quantity < 0 AND movement_at >= NOW() - INTERVAL '90 days' THEN ABS(quantity) ELSE 0 END) AS usage_90_days
    FROM stock_movements
    GROUP BY product_id, warehouse_id, store_id
),
location_slow_dead AS (
    SELECT 
        cs.company_id,
        cs.product_id,
        cs.warehouse_id,
        cs.store_id,
        cs.quantity AS on_hand_quantity,
        COALESCE(la.last_movement_at, '1970-01-01'::TIMESTAMP) AS last_movement_at,
        COALESCE(la.usage_90_days, 0.00) AS usage_90_days,
        (COALESCE(la.last_movement_at, '1970-01-01'::TIMESTAMP) < NOW() - INTERVAL '180 days') AS is_dead,
        (COALESCE(la.last_movement_at, '1970-01-01'::TIMESTAMP) >= NOW() - INTERVAL '180 days' 
         AND COALESCE(la.usage_90_days, 0.00) < cs.quantity * 0.10) AS is_slow
    FROM current_stock cs
    LEFT JOIN latest_activity la ON la.product_id = cs.product_id 
        AND COALESCE(la.warehouse_id, 0) = COALESCE(cs.warehouse_id, 0) 
        AND COALESCE(la.store_id, 0) = COALESCE(cs.store_id, 0)
),
company_slow_dead AS (
    SELECT 
        cs.company_id,
        cs.product_id,
        NULL::BIGINT AS warehouse_id,
        NULL::BIGINT AS store_id,
        SUM(cs.quantity) AS on_hand_quantity,
        MAX(COALESCE(la.last_movement_at, '1970-01-01'::TIMESTAMP)) AS last_movement_at,
        SUM(COALESCE(la.usage_90_days, 0.00)) AS usage_90_days,
        (MAX(COALESCE(la.last_movement_at, '1970-01-01'::TIMESTAMP)) < NOW() - INTERVAL '180 days') AS is_dead,
        (MAX(COALESCE(la.last_movement_at, '1970-01-01'::TIMESTAMP)) >= NOW() - INTERVAL '180 days' 
         AND SUM(COALESCE(la.usage_90_days, 0.00)) < SUM(cs.quantity) * 0.10) AS is_slow
    FROM current_stock cs
    LEFT JOIN latest_activity la ON la.product_id = cs.product_id 
        AND COALESCE(la.warehouse_id, 0) = COALESCE(cs.warehouse_id, 0) 
        AND COALESCE(la.store_id, 0) = COALESCE(cs.store_id, 0)
    GROUP BY cs.company_id, cs.product_id
)
SELECT * FROM location_slow_dead
UNION ALL
SELECT * FROM company_slow_dead
WITH NO DATA;


-- View 5: mv_inventory_replenishment_metrics
CREATE MATERIALIZED VIEW mv_inventory_replenishment_metrics AS
WITH unique_products_count AS (
    SELECT 
        r.company_id,
        s.warehouse_id,
        s.store_id,
        COUNT(DISTINCT s.product_id) AS total_prod_count
    FROM inventory_stock s
    LEFT JOIN warehouses w ON s.warehouse_id = w.id
    LEFT JOIN stores st ON s.store_id = st.id
    JOIN regions r ON r.id = COALESCE(w.region_id, st.region_id)
    GROUP BY r.company_id, s.warehouse_id, s.store_id
),
unique_products_company AS (
    SELECT 
        r.company_id,
        COUNT(DISTINCT s.product_id) AS total_prod_count
    FROM inventory_stock s
    LEFT JOIN warehouses w ON s.warehouse_id = w.id
    LEFT JOIN stores st ON s.store_id = st.id
    JOIN regions r ON r.id = COALESCE(w.region_id, st.region_id)
    GROUP BY r.company_id
),
rules_stats AS (
    SELECT 
        company_id,
        warehouse_id,
        store_id,
        COUNT(*) AS rules_count
    FROM replenishment_rules
    WHERE active = TRUE
    GROUP BY company_id, warehouse_id, store_id
),
rules_company AS (
    SELECT 
        company_id,
        COUNT(*) AS rules_count
    FROM replenishment_rules
    WHERE active = TRUE
    GROUP BY company_id
),
suggestion_stats AS (
    SELECT 
        company_id,
        warehouse_id,
        store_id,
        AVG(EXTRACT(EPOCH FROM (COALESCE(ordered_at, evaluated_at) - evaluated_at)) / 3600.0) AS avg_cycle_time_hours,
        COUNT(CASE WHEN status = 'FULFILLED' THEN 1 END) * 100.0 / NULLIF(COUNT(CASE WHEN status IN ('FULFILLED', 'CANCELLED') THEN 1 END), 0) AS stockout_prevention_rate
    FROM replenishment_suggestions
    GROUP BY company_id, warehouse_id, store_id
),
suggestion_company AS (
    SELECT 
        company_id,
        AVG(EXTRACT(EPOCH FROM (COALESCE(ordered_at, evaluated_at) - evaluated_at)) / 3600.0) AS avg_cycle_time_hours,
        COUNT(CASE WHEN status = 'FULFILLED' THEN 1 END) * 100.0 / NULLIF(COUNT(CASE WHEN status IN ('FULFILLED', 'CANCELLED') THEN 1 END), 0) AS stockout_prevention_rate
    FROM replenishment_suggestions
    GROUP BY company_id
),
location_metrics AS (
    SELECT 
        upc.company_id,
        upc.warehouse_id,
        upc.store_id,
        COALESCE(rs.rules_count, 0) AS rules_count,
        COALESCE(rs.rules_count * 100.0 / NULLIF(upc.total_prod_count, 0), 0.00) AS rules_coverage_percent,
        COALESCE(ss.avg_cycle_time_hours, 0.00) AS avg_replenishment_cycle_time_hours,
        COALESCE(ss.stockout_prevention_rate, 100.00) AS stockout_prevention_rate
    FROM unique_products_count upc
    LEFT JOIN rules_stats rs ON rs.company_id = upc.company_id 
        AND COALESCE(rs.warehouse_id, 0) = COALESCE(upc.warehouse_id, 0) 
        AND COALESCE(rs.store_id, 0) = COALESCE(upc.store_id, 0)
    LEFT JOIN suggestion_stats ss ON ss.company_id = upc.company_id 
        AND COALESCE(ss.warehouse_id, 0) = COALESCE(upc.warehouse_id, 0) 
        AND COALESCE(ss.store_id, 0) = COALESCE(upc.store_id, 0)
),
company_metrics AS (
    SELECT 
        upc.company_id,
        NULL::BIGINT AS warehouse_id,
        NULL::BIGINT AS store_id,
        COALESCE(rs.rules_count, 0) AS rules_count,
        COALESCE(rs.rules_count * 100.0 / NULLIF(upc.total_prod_count, 0), 0.00) AS rules_coverage_percent,
        COALESCE(ss.avg_cycle_time_hours, 0.00) AS avg_replenishment_cycle_time_hours,
        COALESCE(ss.stockout_prevention_rate, 100.00) AS stockout_prevention_rate
    FROM unique_products_company upc
    LEFT JOIN rules_company rs ON rs.company_id = upc.company_id
    LEFT JOIN suggestion_company ss ON ss.company_id = upc.company_id
)
SELECT * FROM location_metrics
UNION ALL
SELECT * FROM company_metrics
WITH NO DATA;


-- View 6: mv_inventory_traceability_metrics
CREATE MATERIALIZED VIEW mv_inventory_traceability_metrics AS
WITH recall_stats AS (
    SELECT 
        company_id,
        COUNT(DISTINCT id) AS active_recalls_count,
        COUNT(DISTINCT lot_id) AS recalled_lots_count,
        COUNT(DISTINCT serial_id) AS recalled_serials_count
    FROM inventory_recalls
    GROUP BY company_id
),
trace_stats AS (
    SELECT 
        company_id,
        warehouse_id,
        store_id,
        ABS(SUM(CASE WHEN event_type = 'RECALL' THEN quantity ELSE 0 END)) AS total_recalled_quantity,
        ABS(SUM(CASE WHEN event_type = 'RECALL' THEN quantity ELSE 0 END)) * 100.0 / NULLIF(SUM(CASE WHEN event_type = 'RECEIPT' THEN quantity ELSE 0 END), 0) AS compromise_rate
    FROM inventory_trace_events
    GROUP BY company_id, warehouse_id, store_id
),
trace_company AS (
    SELECT 
        company_id,
        ABS(SUM(CASE WHEN event_type = 'RECALL' THEN quantity ELSE 0 END)) AS total_recalled_quantity,
        ABS(SUM(CASE WHEN event_type = 'RECALL' THEN quantity ELSE 0 END)) * 100.0 / NULLIF(SUM(CASE WHEN event_type = 'RECEIPT' THEN quantity ELSE 0 END), 0) AS compromise_rate
    FROM inventory_trace_events
    GROUP BY company_id
),
location_metrics AS (
    SELECT 
        c.id AS company_id,
        t.warehouse_id,
        t.store_id,
        COALESCE(r.active_recalls_count, 0) AS active_recalls_count,
        COALESCE(r.recalled_lots_count, 0) AS recalled_lots_count,
        COALESCE(r.recalled_serials_count, 0) AS recalled_serials_count,
        COALESCE(ts.total_recalled_quantity, 0.00) AS total_recalled_quantity,
        COALESCE(ts.compromise_rate, 0.00) AS compromise_rate
    FROM companies c
    CROSS JOIN (
        SELECT DISTINCT warehouse_id, store_id FROM inventory_trace_events
        UNION
        SELECT DISTINCT warehouse_id, store_id FROM inventory_serials
    ) t
    LEFT JOIN recall_stats r ON r.company_id = c.id
    LEFT JOIN trace_stats ts ON ts.company_id = c.id 
        AND COALESCE(ts.warehouse_id, 0) = COALESCE(t.warehouse_id, 0) 
        AND COALESCE(ts.store_id, 0) = COALESCE(t.store_id, 0)
    WHERE t.warehouse_id IS NOT NULL OR t.store_id IS NOT NULL
),
company_metrics AS (
    SELECT 
        c.id AS company_id,
        NULL::BIGINT AS warehouse_id,
        NULL::BIGINT AS store_id,
        COALESCE(r.active_recalls_count, 0) AS active_recalls_count,
        COALESCE(r.recalled_lots_count, 0) AS recalled_lots_count,
        COALESCE(r.recalled_serials_count, 0) AS recalled_serials_count,
        COALESCE(tc.total_recalled_quantity, 0.00) AS total_recalled_quantity,
        COALESCE(tc.compromise_rate, 0.00) AS compromise_rate
    FROM companies c
    LEFT JOIN recall_stats r ON r.company_id = c.id
    LEFT JOIN trace_company tc ON tc.company_id = c.id
)
SELECT * FROM location_metrics
UNION ALL
SELECT * FROM company_metrics
WITH NO DATA;


-- View 7: mv_inventory_turnover
CREATE MATERIALIZED VIEW mv_inventory_turnover AS
WITH product_avg_price AS (
    SELECT product_id, COALESCE(AVG(unit_price), 0.00) AS avg_price
    FROM purchase_order_items
    GROUP BY product_id
),
current_inventory_value AS (
    SELECT 
        s.warehouse_id,
        s.store_id,
        r.company_id,
        SUM(s.quantity * COALESCE(ap.avg_price, 0.00)) AS stock_value
    FROM inventory_stock s
    LEFT JOIN warehouses w ON s.warehouse_id = w.id
    LEFT JOIN stores st ON s.store_id = st.id
    JOIN regions r ON r.id = COALESCE(w.region_id, st.region_id)
    LEFT JOIN product_avg_price ap ON ap.product_id = s.product_id
    GROUP BY s.warehouse_id, s.store_id, r.company_id
),
cogs_by_location AS (
    SELECT 
        st.company_id,
        NULL::BIGINT AS warehouse_id,
        st.store_id,
        SUM(sti.quantity * COALESCE(ap.avg_price, 0.00)) AS total_cogs
    FROM sales_transaction_items sti
    JOIN sales_transactions st ON sti.sales_transaction_id = st.id
    LEFT JOIN product_avg_price ap ON ap.product_id = sti.product_id
    WHERE st.status = 'COMPLETED' AND st.transaction_time >= NOW() - INTERVAL '365 days'
    GROUP BY st.company_id, st.store_id
    
    UNION ALL
    
    SELECT 
        it.company_id,
        it.source_warehouse_id AS warehouse_id,
        NULL::BIGINT AS store_id,
        SUM(iti.quantity * COALESCE(ap.avg_price, 0.00)) AS total_cogs
    FROM inventory_transfer_items iti
    JOIN inventory_transfers it ON iti.transfer_id = it.id
    LEFT JOIN product_avg_price ap ON ap.product_id = iti.product_id
    WHERE it.status IN ('IN_TRANSIT', 'RECEIVED', 'CLOSED') AND it.dispatched_at >= NOW() - INTERVAL '365 days'
    GROUP BY it.company_id, it.source_warehouse_id
),
location_turnover AS (
    SELECT 
        civ.company_id,
        civ.warehouse_id,
        civ.store_id,
        COALESCE(cogs.total_cogs, 0.00) AS cost_of_goods_sold,
        civ.stock_value AS average_inventory_value,
        CASE 
            WHEN civ.stock_value > 0 THEN COALESCE(cogs.total_cogs, 0.00) / civ.stock_value
            ELSE 0.00
        END AS inventory_turnover_ratio,
        CASE 
            WHEN COALESCE(cogs.total_cogs, 0.00) > 0 AND civ.stock_value > 0 THEN 
                365.0 / (COALESCE(cogs.total_cogs, 0.00) / civ.stock_value)
            ELSE 365.0
        END AS days_on_hand
    FROM current_inventory_value civ
    LEFT JOIN (
        SELECT company_id, warehouse_id, store_id, SUM(total_cogs) AS total_cogs
        FROM cogs_by_location
        GROUP BY company_id, warehouse_id, store_id
    ) cogs ON cogs.company_id = civ.company_id 
        AND COALESCE(cogs.warehouse_id, 0) = COALESCE(civ.warehouse_id, 0) 
        AND COALESCE(cogs.store_id, 0) = COALESCE(civ.store_id, 0)
),
company_turnover AS (
    SELECT 
        civ.company_id,
        NULL::BIGINT AS warehouse_id,
        NULL::BIGINT AS store_id,
        SUM(COALESCE(cogs.total_cogs, 0.00)) AS cost_of_goods_sold,
        SUM(civ.stock_value) AS average_inventory_value,
        CASE 
            WHEN SUM(civ.stock_value) > 0 THEN SUM(COALESCE(cogs.total_cogs, 0.00)) / SUM(civ.stock_value)
            ELSE 0.00
        END AS inventory_turnover_ratio,
        CASE 
            WHEN SUM(COALESCE(cogs.total_cogs, 0.00)) > 0 AND SUM(civ.stock_value) > 0 THEN 
                365.0 / (SUM(COALESCE(cogs.total_cogs, 0.00)) / SUM(civ.stock_value))
            ELSE 365.0
        END AS days_on_hand
    FROM current_inventory_value civ
    LEFT JOIN (
        SELECT company_id, warehouse_id, store_id, SUM(total_cogs) AS total_cogs
        FROM cogs_by_location
        GROUP BY company_id, warehouse_id, store_id
    ) cogs ON cogs.company_id = civ.company_id 
        AND COALESCE(cogs.warehouse_id, 0) = COALESCE(civ.warehouse_id, 0) 
        AND COALESCE(cogs.store_id, 0) = COALESCE(civ.store_id, 0)
    GROUP BY civ.company_id
)
SELECT * FROM location_turnover
UNION ALL
SELECT * FROM company_turnover
WITH NO DATA;


-- 4. Unique Indexes for Concurrent Refresh
CREATE UNIQUE INDEX uidx_mv_inventory_kpis ON mv_inventory_kpis (company_id, warehouse_id, store_id);
CREATE UNIQUE INDEX uidx_mv_inventory_aging_expiry ON mv_inventory_aging_expiry (company_id, warehouse_id, store_id);
CREATE UNIQUE INDEX uidx_mv_inventory_abc_xyz ON mv_inventory_abc_xyz (company_id, product_id, warehouse_id, store_id);
CREATE UNIQUE INDEX uidx_mv_inventory_slow_dead ON mv_inventory_slow_dead (company_id, product_id, warehouse_id, store_id);
CREATE UNIQUE INDEX uidx_mv_inventory_repl_metrics ON mv_inventory_replenishment_metrics (company_id, warehouse_id, store_id);
CREATE UNIQUE INDEX uidx_mv_inventory_trace_metrics ON mv_inventory_traceability_metrics (company_id, warehouse_id, store_id);
CREATE UNIQUE INDEX uidx_mv_inventory_turnover ON mv_inventory_turnover (company_id, warehouse_id, store_id);
