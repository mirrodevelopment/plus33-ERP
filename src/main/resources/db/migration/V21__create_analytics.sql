-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 21
-- File              : V21__create_analytics.sql
-- Operation Type    : Schema Creation
-- Purpose           : create analytics
--
-- Tables Created    : dashboard_configs
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : idx_dashboard_configs_company, idx_dashboard_role, uidx_mv_sales_daily, uidx_mv_inventory_levels, uidx_mv_attendance_daily, uidx_mv_financial_summary, idx_mv_sales_daily_date, idx_mv_financial_summary_date, idx_mv_attendance_daily_status
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V21__create_analytics.sql
-- PLUS33 ERP — Analytics & Dashboards module schema
-- ============================================================

CREATE TABLE dashboard_configs (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    dashboard_code VARCHAR(100) NOT NULL,
    dashboard_name VARCHAR(150) NOT NULL,
    role_code VARCHAR(100) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    refresh_interval_minutes INTEGER NOT NULL DEFAULT 15,
    last_refreshed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_dashboard_configs_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id),

    CONSTRAINT uk_dashboard_company_role
        UNIQUE (company_id, dashboard_code, role_code),

    CONSTRAINT chk_dashboard_refresh_interval
        CHECK (refresh_interval_minutes > 0)
);

CREATE INDEX idx_dashboard_configs_company ON dashboard_configs(company_id);
CREATE INDEX idx_dashboard_role ON dashboard_configs(company_id, role_code);

-- Materialized Views (WITH NO DATA)

CREATE MATERIALIZED VIEW mv_sales_daily AS
SELECT
    st.company_id,
    st.store_id,
    DATE(st.transaction_time) AS sales_date,
    COUNT(*) AS total_transactions,
    SUM(st.total_amount) AS total_sales
FROM sales_transactions st
GROUP BY st.company_id, st.store_id, DATE(st.transaction_time)
WITH NO DATA;

CREATE MATERIALIZED VIEW mv_inventory_levels AS
SELECT
    i.id AS inventory_stock_id,
    COALESCE(rw.company_id, rs.company_id) AS company_id,
    i.product_id,
    i.warehouse_id,
    i.store_id,
    i.quantity
FROM inventory_stock i
LEFT JOIN warehouses w ON w.id = i.warehouse_id
LEFT JOIN regions rw ON rw.id = w.region_id
LEFT JOIN stores s ON s.id = i.store_id
LEFT JOIN regions rs ON rs.id = s.region_id
WITH NO DATA;

CREATE MATERIALIZED VIEW mv_attendance_daily AS
SELECT
    e.company_id,
    a.employee_id,
    a.attendance_date,
    a.status
FROM attendance a
JOIN employees e ON e.id = a.employee_id
WITH NO DATA;

CREATE MATERIALIZED VIEW mv_financial_summary AS
SELECT
    je.company_id,
    je.entry_date,
    SUM(jel.debit_amount) AS total_debits,
    SUM(jel.credit_amount) AS total_credits
FROM journal_entries je
JOIN journal_entry_lines jel ON je.id = jel.journal_entry_id
WHERE je.status = 'POSTED'
GROUP BY je.company_id, je.entry_date
WITH NO DATA;

-- Unique Indexes for Concurrent Refresh
CREATE UNIQUE INDEX uidx_mv_sales_daily ON mv_sales_daily (company_id, store_id, sales_date);
CREATE UNIQUE INDEX uidx_mv_inventory_levels ON mv_inventory_levels (inventory_stock_id);
CREATE UNIQUE INDEX uidx_mv_attendance_daily ON mv_attendance_daily (company_id, employee_id, attendance_date);
CREATE UNIQUE INDEX uidx_mv_financial_summary ON mv_financial_summary (company_id, entry_date);

-- Filtering Performance Indexes
CREATE INDEX idx_mv_sales_daily_date ON mv_sales_daily (sales_date);
CREATE INDEX idx_mv_financial_summary_date ON mv_financial_summary (entry_date);
CREATE INDEX idx_mv_attendance_daily_status ON mv_attendance_daily (status);
