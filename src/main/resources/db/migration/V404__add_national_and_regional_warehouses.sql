-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Migration Version : 404
-- File              : V404__add_national_and_regional_warehouses.sql
-- Operation Type    : Data Seeding
-- Purpose           : Add national and regional warehouses, zones, and standard storage locations
-- ============================================================================

-- 1. Insert National Warehouses (linked to Country Regions)
INSERT INTO warehouses (code, name, address, phone, email, timezone, opening_date, region_id, active, created_at, updated_at)
VALUES 
('WH_FR_NAT', 'France National Warehouse', '10 Rue de la Nation, Paris', '+33-1-00-00-01', 'france.nat.wh@plus33coffee.fr', 'Europe/Paris', '2026-07-01', 7, TRUE, NOW(), NOW()),
('WH_AE_NAT', 'UAE National Warehouse', 'Sheikh Zayed Road, Dubai', '+971-4-00-00-01', 'uae.nat.wh@plus33coffee.fr', 'Asia/Dubai', '2026-07-01', 8, TRUE, NOW(), NOW()),
('WH_IN_NAT', 'India National Warehouse', 'Connaught Place, New Delhi', '+91-11-00-00-01', 'india.nat.wh@plus33coffee.fr', 'Asia/Kolkata', '2026-07-01', 9, TRUE, NOW(), NOW())
ON CONFLICT (code) DO NOTHING;

-- 2. Insert Regional Warehouses for remaining sub-regions
INSERT INTO warehouses (code, name, address, phone, email, timezone, opening_date, region_id, active, created_at, updated_at)
VALUES
('WH_FR_REG_1', 'Ile-de-France Regional Warehouse', 'Paris Region Center, Paris', '+33-1-00-00-11', 'idf.wh@plus33coffee.fr', 'Europe/Paris', '2026-07-01', 13, TRUE, NOW(), NOW()),
('WH_FR_REG_2', 'PACA Regional Warehouse', 'Marseille Industrial Park, Marseille', '+33-4-00-00-12', 'paca.wh@plus33coffee.fr', 'Europe/Paris', '2026-07-01', 14, TRUE, NOW(), NOW()),
('WH_FR_REG_3', 'ARA Regional Warehouse', 'Lyon Logistics Hub, Lyon', '+33-4-00-00-13', 'ara.wh@plus33coffee.fr', 'Europe/Paris', '2026-07-01', 15, TRUE, NOW(), NOW()),

('WH_AE_REG_1', 'Abu Dhabi Regional Warehouse', 'Mussafah Industrial Area, Abu Dhabi', '+971-2-00-00-11', 'abudhabi.wh@plus33coffee.fr', 'Asia/Dubai', '2026-07-01', 16, TRUE, NOW(), NOW()),
('WH_AE_REG_2', 'Sharjah Regional Warehouse', 'Sharjah Industrial Zone, Sharjah', '+971-6-00-00-12', 'sharjah.wh@plus33coffee.fr', 'Asia/Dubai', '2026-07-01', 17, TRUE, NOW(), NOW()),
('WH_AE_REG_3', 'Ajman Regional Warehouse', 'Ajman Free Zone, Ajman', '+971-6-00-00-13', 'ajman.wh@plus33coffee.fr', 'Asia/Dubai', '2026-07-01', 18, TRUE, NOW(), NOW()),

('WH_IN_REG_1', 'West India Regional Warehouse', 'MIDC Industrial Area, Mumbai', '+91-22-00-00-11', 'westindia.wh@plus33coffee.fr', 'Asia/Kolkata', '2026-07-01', 19, TRUE, NOW(), NOW()),
('WH_IN_REG_2', 'North India Regional Warehouse', 'Okhla Industrial Area, New Delhi', '+91-11-00-00-12', 'northindia.wh@plus33coffee.fr', 'Asia/Kolkata', '2026-07-01', 20, TRUE, NOW(), NOW()),
('WH_IN_REG_3', 'East India Regional Warehouse', 'Salt Lake Sector V, Kolkata', '+91-33-00-00-13', 'eastindia.wh@plus33coffee.fr', 'Asia/Kolkata', '2026-07-01', 21, TRUE, NOW(), NOW())
ON CONFLICT (code) DO NOTHING;

-- 3. Insert Default Zones for New Warehouses
INSERT INTO warehouse_zones (company_id, warehouse_id, code, name, zone_type, active, created_at, updated_at)
SELECT 
    3, -- company_id
    w.id, -- warehouse_id
    'ZONE-STD', -- code
    'Standard Storage Zone', -- name
    'BULK', -- zone_type
    TRUE,
    NOW(),
    NOW()
FROM warehouses w
WHERE w.code IN ('WH_FR_NAT', 'WH_AE_NAT', 'WH_IN_NAT', 'WH_FR_REG_1', 'WH_FR_REG_2', 'WH_FR_REG_3', 'WH_AE_REG_1', 'WH_AE_REG_2', 'WH_AE_REG_3', 'WH_IN_REG_1', 'WH_IN_REG_2', 'WH_IN_REG_3')
ON CONFLICT DO NOTHING;

-- 4. Insert Default Locations for New Warehouses
INSERT INTO warehouse_locations (company_id, warehouse_id, zone_id, location_code, location_type, active, created_at, updated_at)
SELECT 
    3, -- company_id
    w.id, -- warehouse_id
    z.id, -- zone_id
    'LOC-STD-01', -- location_code
    'BULK', -- location_type
    TRUE,
    NOW(),
    NOW()
FROM warehouses w
JOIN warehouse_zones z ON z.warehouse_id = w.id AND z.code = 'ZONE-STD'
WHERE w.code IN ('WH_FR_NAT', 'WH_AE_NAT', 'WH_IN_NAT', 'WH_FR_REG_1', 'WH_FR_REG_2', 'WH_FR_REG_3', 'WH_AE_REG_1', 'WH_AE_REG_2', 'WH_AE_REG_3', 'WH_IN_REG_1', 'WH_IN_REG_2', 'WH_IN_REG_3')
ON CONFLICT DO NOTHING;
