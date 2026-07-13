-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 377
-- File              : V377__seed_plus33_coffee_company_org.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed plus33 coffee company org
--
-- Tables Created    : N/A
-- Tables Altered    : N/A
-- Seed Data For     : companies, departments, regions, stores, warehouses
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V377__seed_plus33_coffee_company_org.sql
-- PLUS33 ERP — PLUS33 Coffee (France) Company & Organization
-- ============================================================

-- ── Company ─────────────────────────────────────────────────
INSERT INTO companies (code, name, legal_name, country_code, active, fiscal_year_start_month, fiscal_year_start_day)
VALUES ('PLUS33_COFFEE', 'PLUS33 Coffee', 'PLUS33 Coffee SAS', 'FR', TRUE, 1, 1);

-- ── Regions (5) ─────────────────────────────────────────────
INSERT INTO regions (code, name, description, company_id)
SELECT 'IDF', 'Île-de-France', 'Paris metropolitan region — headquarters and flagship stores', c.id FROM companies c WHERE c.code = 'PLUS33_COFFEE';

INSERT INTO regions (code, name, description, company_id)
SELECT 'PACA', 'Provence-Alpes-Côte d''Azur', 'Southern France Mediterranean coast operations', c.id FROM companies c WHERE c.code = 'PLUS33_COFFEE';

INSERT INTO regions (code, name, description, company_id)
SELECT 'ARA', 'Auvergne-Rhône-Alpes', 'Lyon and Alpine region operations', c.id FROM companies c WHERE c.code = 'PLUS33_COFFEE';

INSERT INTO regions (code, name, description, company_id)
SELECT 'OCC', 'Occitanie', 'Toulouse and Montpellier region operations', c.id FROM companies c WHERE c.code = 'PLUS33_COFFEE';

INSERT INTO regions (code, name, description, company_id)
SELECT 'NAQ', 'Nouvelle-Aquitaine', 'Bordeaux and southwest France operations', c.id FROM companies c WHERE c.code = 'PLUS33_COFFEE';

-- ── Warehouses (2) ──────────────────────────────────────────
INSERT INTO warehouses (code, name, address, phone, email, timezone, opening_date, region_id, active)
SELECT 'WH_PARIS', 'Paris Central Warehouse', '45 Rue de la Logistique, 93200 Saint-Denis', '+33-1-42-33-00-01', 'paris.wh@plus33coffee.fr', 'Europe/Paris', '2024-01-15', r.id, TRUE
FROM regions r WHERE r.code = 'IDF';

INSERT INTO warehouses (code, name, address, phone, email, timezone, opening_date, region_id, active)
SELECT 'WH_LYON', 'Lyon Distribution Center', '12 Avenue de l''Industrie, 69008 Lyon', '+33-4-72-33-00-02', 'lyon.wh@plus33coffee.fr', 'Europe/Paris', '2024-03-01', r.id, TRUE
FROM regions r WHERE r.code = 'ARA';

-- ── Stores (10 — 2 per region) ──────────────────────────────

-- Île-de-France stores
INSERT INTO stores (code, name, address, phone, email, timezone, opening_date, region_id, warehouse_id, active)
SELECT 'ST_PARIS_01', 'PLUS33 Coffee — Le Marais', '18 Rue des Francs-Bourgeois, 75004 Paris', '+33-1-42-71-10-01', 'lemarais@plus33coffee.fr', 'Europe/Paris', '2024-02-01', r.id, w.id, TRUE
FROM regions r CROSS JOIN warehouses w WHERE r.code = 'IDF' AND w.code = 'WH_PARIS';

INSERT INTO stores (code, name, address, phone, email, timezone, opening_date, region_id, warehouse_id, active)
SELECT 'ST_PARIS_02', 'PLUS33 Coffee — Saint-Germain', '42 Boulevard Saint-Germain, 75005 Paris', '+33-1-43-25-10-02', 'saintgermain@plus33coffee.fr', 'Europe/Paris', '2024-02-15', r.id, w.id, TRUE
FROM regions r CROSS JOIN warehouses w WHERE r.code = 'IDF' AND w.code = 'WH_PARIS';

-- Provence-Alpes-Côte d'Azur stores
INSERT INTO stores (code, name, address, phone, email, timezone, opening_date, region_id, warehouse_id, active)
SELECT 'ST_MARS_01', 'PLUS33 Coffee — Vieux-Port', '8 Quai du Port, 13002 Marseille', '+33-4-91-54-10-03', 'vieuxport@plus33coffee.fr', 'Europe/Paris', '2024-03-01', r.id, w.id, TRUE
FROM regions r CROSS JOIN warehouses w WHERE r.code = 'PACA' AND w.code = 'WH_PARIS';

INSERT INTO stores (code, name, address, phone, email, timezone, opening_date, region_id, warehouse_id, active)
SELECT 'ST_NICE_01', 'PLUS33 Coffee — Promenade des Anglais', '22 Promenade des Anglais, 06000 Nice', '+33-4-93-87-10-04', 'nice@plus33coffee.fr', 'Europe/Paris', '2024-03-15', r.id, w.id, TRUE
FROM regions r CROSS JOIN warehouses w WHERE r.code = 'PACA' AND w.code = 'WH_PARIS';

-- Auvergne-Rhône-Alpes stores
INSERT INTO stores (code, name, address, phone, email, timezone, opening_date, region_id, warehouse_id, active)
SELECT 'ST_LYON_01', 'PLUS33 Coffee — Presqu''île', '15 Rue de la République, 69002 Lyon', '+33-4-72-40-10-05', 'presquile@plus33coffee.fr', 'Europe/Paris', '2024-04-01', r.id, w.id, TRUE
FROM regions r CROSS JOIN warehouses w WHERE r.code = 'ARA' AND w.code = 'WH_LYON';

INSERT INTO stores (code, name, address, phone, email, timezone, opening_date, region_id, warehouse_id, active)
SELECT 'ST_GREN_01', 'PLUS33 Coffee — Grenoble Centre', '5 Place Grenette, 38000 Grenoble', '+33-4-76-46-10-06', 'grenoble@plus33coffee.fr', 'Europe/Paris', '2024-04-15', r.id, w.id, TRUE
FROM regions r CROSS JOIN warehouses w WHERE r.code = 'ARA' AND w.code = 'WH_LYON';

-- Occitanie stores
INSERT INTO stores (code, name, address, phone, email, timezone, opening_date, region_id, warehouse_id, active)
SELECT 'ST_TLSE_01', 'PLUS33 Coffee — Place du Capitole', '3 Place du Capitole, 31000 Toulouse', '+33-5-61-23-10-07', 'capitole@plus33coffee.fr', 'Europe/Paris', '2024-05-01', r.id, w.id, TRUE
FROM regions r CROSS JOIN warehouses w WHERE r.code = 'OCC' AND w.code = 'WH_LYON';

INSERT INTO stores (code, name, address, phone, email, timezone, opening_date, region_id, warehouse_id, active)
SELECT 'ST_MTPL_01', 'PLUS33 Coffee — Place de la Comédie', '10 Place de la Comédie, 34000 Montpellier', '+33-4-67-60-10-08', 'comedie@plus33coffee.fr', 'Europe/Paris', '2024-05-15', r.id, w.id, TRUE
FROM regions r CROSS JOIN warehouses w WHERE r.code = 'OCC' AND w.code = 'WH_LYON';

-- Nouvelle-Aquitaine stores
INSERT INTO stores (code, name, address, phone, email, timezone, opening_date, region_id, warehouse_id, active)
SELECT 'ST_BORD_01', 'PLUS33 Coffee — Quai des Chartrons', '28 Quai des Chartrons, 33000 Bordeaux', '+33-5-56-44-10-09', 'chartrons@plus33coffee.fr', 'Europe/Paris', '2024-06-01', r.id, w.id, TRUE
FROM regions r CROSS JOIN warehouses w WHERE r.code = 'NAQ' AND w.code = 'WH_PARIS';

INSERT INTO stores (code, name, address, phone, email, timezone, opening_date, region_id, warehouse_id, active)
SELECT 'ST_LARO_01', 'PLUS33 Coffee — Vieux Port La Rochelle', '14 Quai Duperré, 17000 La Rochelle', '+33-5-46-41-10-10', 'larochelle@plus33coffee.fr', 'Europe/Paris', '2024-06-15', r.id, w.id, TRUE
FROM regions r CROSS JOIN warehouses w WHERE r.code = 'NAQ' AND w.code = 'WH_PARIS';

-- ── Departments (10) ────────────────────────────────────────
INSERT INTO departments (company_id, code, name, active)
SELECT c.id, d.code, d.name, TRUE
FROM companies c
CROSS JOIN (VALUES
    ('DEPT_FIN',   'Finance'),
    ('DEPT_HR',    'Human Resources'),
    ('DEPT_SALES', 'Sales'),
    ('DEPT_INV',   'Inventory'),
    ('DEPT_WH',    'Warehouse'),
    ('DEPT_PROC',  'Procurement'),
    ('DEPT_CRM',   'CRM'),
    ('DEPT_CS',    'Customer Support'),
    ('DEPT_ADMIN', 'Administration'),
    ('DEPT_IT',    'IT')
) AS d(code, name)
WHERE c.code = 'PLUS33_COFFEE';
