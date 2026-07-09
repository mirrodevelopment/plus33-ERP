-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 174
-- File              : V174__create_materialized_views.sql
-- Operation Type    : DDL
-- Purpose           : create materialized views
--
-- Tables Created    : N/A
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V174: BI Materialized Views
-- Purpose: Precomputed OLAP aggregations for high-performance dashboard queries.
-- These are standard PostgreSQL VIEWs (not MATERIALIZED VIEWs) for portability
-- with H2 test environments; refresh is managed by CubeRefreshService in Java.

CREATE OR REPLACE VIEW mv_finance_monthly AS
SELECT
    f.company_id,
    d.year_number,
    d.month_number,
    d.month_name,
    d.quarter_number,
    a.account_code,
    a.account_name,
    a.account_type,
    SUM(f.debit_amount)  AS total_debit,
    SUM(f.credit_amount) AS total_credit,
    SUM(f.net_amount)    AS net_amount,
    COUNT(*)             AS transaction_count
FROM fact_finance f
JOIN dim_date d      ON f.date_key = d.date_key
LEFT JOIN dim_account a ON f.account_dim_id = a.id
GROUP BY f.company_id, d.year_number, d.month_number, d.month_name,
         d.quarter_number, a.account_code, a.account_name, a.account_type;

CREATE OR REPLACE VIEW mv_sales_monthly AS
SELECT
    f.company_id,
    d.year_number,
    d.month_number,
    d.month_name,
    d.quarter_number,
    c.customer_name,
    c.segment,
    c.region,
    p.product_category,
    f.sales_channel,
    f.sales_region,
    SUM(f.quantity)      AS total_quantity,
    SUM(f.net_amount)    AS total_net_amount,
    SUM(f.gross_amount)  AS total_gross_amount,
    SUM(f.gross_margin)  AS total_gross_margin,
    COUNT(*)             AS order_count
FROM fact_sales f
JOIN dim_date d          ON f.date_key = d.date_key
LEFT JOIN dim_customer c ON f.customer_dim_id = c.id
LEFT JOIN dim_product p  ON f.product_dim_id = p.id
GROUP BY f.company_id, d.year_number, d.month_number, d.month_name,
         d.quarter_number, c.customer_name, c.segment, c.region,
         p.product_category, f.sales_channel, f.sales_region;

CREATE OR REPLACE VIEW mv_inventory_monthly AS
SELECT
    f.company_id,
    d.year_number,
    d.month_number,
    d.month_name,
    p.product_category,
    l.location_name,
    l.location_type,
    f.movement_type,
    SUM(f.quantity_in)   AS total_in,
    SUM(f.quantity_out)  AS total_out,
    SUM(f.total_value)   AS total_value,
    AVG(f.closing_stock) AS avg_closing_stock
FROM fact_inventory f
JOIN dim_date d           ON f.date_key = d.date_key
LEFT JOIN dim_product p   ON f.product_dim_id = p.id
LEFT JOIN dim_location l  ON f.location_dim_id = l.id
GROUP BY f.company_id, d.year_number, d.month_number, d.month_name,
         p.product_category, l.location_name, l.location_type, f.movement_type;

CREATE OR REPLACE VIEW mv_payroll_monthly AS
SELECT
    f.company_id,
    d.year_number,
    d.month_number,
    d.month_name,
    o.org_name,
    SUM(f.gross_salary)  AS total_gross,
    SUM(f.deductions)    AS total_deductions,
    SUM(f.net_salary)    AS total_net,
    SUM(f.employer_cost) AS total_employer_cost,
    COUNT(*)             AS employee_count
FROM fact_payroll f
JOIN dim_date d              ON f.date_key = d.date_key
LEFT JOIN dim_organization o ON f.org_dim_id = o.id
GROUP BY f.company_id, d.year_number, d.month_number, d.month_name, o.org_name;

CREATE OR REPLACE VIEW mv_manufacturing_monthly AS
SELECT
    f.company_id,
    d.year_number,
    d.month_number,
    d.month_name,
    p.product_category,
    f.work_center,
    SUM(f.planned_qty)           AS total_planned,
    SUM(f.actual_qty)            AS total_actual,
    SUM(f.scrap_qty)             AS total_scrap,
    SUM(f.total_production_cost) AS total_cost,
    SUM(f.labor_hours)           AS total_labor_hours,
    CASE WHEN SUM(f.planned_qty) > 0
         THEN ROUND((SUM(f.actual_qty) / SUM(f.planned_qty)) * 100, 2)
         ELSE 0 END              AS oee_pct
FROM fact_manufacturing f
JOIN dim_date d          ON f.date_key = d.date_key
LEFT JOIN dim_product p  ON f.product_dim_id = p.id
GROUP BY f.company_id, d.year_number, d.month_number, d.month_name,
         p.product_category, f.work_center;

CREATE OR REPLACE VIEW mv_procurement_monthly AS
SELECT
    f.company_id,
    d.year_number,
    d.month_number,
    d.month_name,
    s.supplier_name,
    s.supplier_category,
    p.product_category,
    SUM(f.quantity)      AS total_quantity,
    SUM(f.gross_amount)  AS total_spend,
    AVG(f.lead_time_days) AS avg_lead_time
FROM fact_procurement f
JOIN dim_date d           ON f.date_key = d.date_key
LEFT JOIN dim_supplier s  ON f.supplier_dim_id = s.id
LEFT JOIN dim_product p   ON f.product_dim_id = p.id
GROUP BY f.company_id, d.year_number, d.month_number, d.month_name,
         s.supplier_name, s.supplier_category, p.product_category;

CREATE OR REPLACE VIEW mv_hcm_monthly AS
SELECT
    f.company_id,
    d.year_number,
    d.month_number,
    d.month_name,
    o.org_name,
    SUM(f.headcount)       AS total_headcount,
    SUM(f.new_hires)       AS total_new_hires,
    SUM(f.attrition_count) AS total_attrition,
    AVG(f.performance_score) AS avg_performance,
    CASE WHEN SUM(f.headcount) > 0
         THEN ROUND((SUM(f.attrition_count)::NUMERIC / SUM(f.headcount)) * 100, 2)
         ELSE 0 END AS attrition_rate_pct
FROM fact_hcm f
JOIN dim_date d              ON f.date_key = d.date_key
LEFT JOIN dim_organization o ON f.org_dim_id = o.id
GROUP BY f.company_id, d.year_number, d.month_number, d.month_name, o.org_name;

CREATE OR REPLACE VIEW mv_executive_summary AS
SELECT
    fs.company_id,
    d.year_number,
    d.quarter_number,
    d.month_number,
    SUM(fs.gross_amount)  AS total_revenue,
    SUM(fs.gross_margin)  AS total_gross_margin,
    CASE WHEN SUM(fs.gross_amount) > 0
         THEN ROUND((SUM(fs.gross_margin) / SUM(fs.gross_amount)) * 100, 2)
         ELSE 0 END       AS gross_margin_pct,
    SUM(fp.gross_salary)  AS total_payroll_cost,
    SUM(fpr.gross_amount) AS total_procurement_spend
FROM fact_sales fs
JOIN dim_date d                         ON fs.date_key = d.date_key
LEFT JOIN fact_payroll fp               ON fp.company_id = fs.company_id AND fp.date_key = fs.date_key
LEFT JOIN fact_procurement fpr          ON fpr.company_id = fs.company_id AND fpr.date_key = fs.date_key
GROUP BY fs.company_id, d.year_number, d.quarter_number, d.month_number;
