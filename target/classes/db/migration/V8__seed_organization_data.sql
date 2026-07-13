-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 8
-- File              : V8__seed_organization_data.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed organization data
--
-- Tables Created    : N/A
-- Tables Altered    : N/A
-- Seed Data For     : companies, regions, stores, warehouses
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V8__seed_organization_data.sql
-- PLUS33 ERP — Initial Organization seed data
-- ============================================================

INSERT INTO companies (code, name, legal_name, country_code, active)
VALUES ('PLUS33_GLOBAL', 'PLUS33 Global Corp', 'PLUS33 Global Limited', 'AE', TRUE);

INSERT INTO regions (code, name, description, company_id)
SELECT 'UAE_REGION', 'UAE Region', 'United Arab Emirates regional operations', id
FROM companies
WHERE code = 'PLUS33_GLOBAL';

INSERT INTO warehouses (code, name, address, phone, email, timezone, opening_date, region_id, active)
SELECT 'DUBAI_WAREHOUSE', 'Dubai Central Warehouse', 'Dubai Al Quoz Industrial Area 3', '+971-4-1234567', 'dubai.wh@plus33.com', 'Asia/Dubai', '2026-01-01', id, TRUE
FROM regions
WHERE code = 'UAE_REGION';

INSERT INTO stores (code, name, address, phone, email, timezone, opening_date, region_id, warehouse_id, active)
SELECT 'DUBAI_MALL_STORE', 'Dubai Mall Flagship Store', 'The Dubai Mall, Ground Floor, G-12', '+971-4-7654321', 'dubaimall@plus33.com', 'Asia/Dubai', '2026-02-01', r.id, w.id, TRUE
FROM regions r
CROSS JOIN warehouses w
WHERE r.code = 'UAE_REGION' AND w.code = 'DUBAI_WAREHOUSE';
