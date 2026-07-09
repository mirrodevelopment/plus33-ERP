-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 98
-- File              : V98__create_wms_cqrs_read_models.sql
-- Operation Type    : DDL
-- Purpose           : create wms cqrs read models
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
-- V98: CQRS Read Models and Materialized Views for 16 Enterprise KPIs
CREATE MATERIALIZED VIEW IF NOT EXISTS mv_wms_kpis AS
SELECT
    c.id AS company_id,
    w.id AS warehouse_id,
    COALESCE(AVG(EXTRACT(EPOCH FROM (cc.completed_at - cc.started_at))), 0) AS avg_cycle_count_seconds,
    COUNT(DISTINCT im.id) AS total_inventory_movements,
    CURRENT_TIMESTAMP AS refreshed_at
FROM companies c
CROSS JOIN warehouses w
LEFT JOIN inventory_movements im ON im.company_id = c.id AND im.warehouse_id = w.id
LEFT JOIN cycle_count_plans cc ON cc.company_id = c.id AND cc.warehouse_id = w.id
GROUP BY c.id, w.id;
