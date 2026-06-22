-- ============================================================
-- V51__seed_inventory_analytics_permissions.sql
-- PLUS33 ERP — Seeding inventory analytics permissions
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('INVENTORY_ANALYTICS_VIEW',    'View Inventory Analytics',    'Allows viewing inventory analytics reports and dashboards'),
('INVENTORY_ANALYTICS_REFRESH', 'Refresh Inventory Analytics', 'Allows manual triggering of inventory materialized views refresh')
ON CONFLICT (code) DO NOTHING;

-- Map INVENTORY_ANALYTICS_VIEW to ULTIMATE_ADMIN, REGIONAL_MANAGER, WAREHOUSE_MANAGER
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code IN ('ULTIMATE_ADMIN', 'REGIONAL_MANAGER', 'WAREHOUSE_MANAGER')
  AND p.code = 'INVENTORY_ANALYTICS_VIEW'
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- Map INVENTORY_ANALYTICS_REFRESH to ULTIMATE_ADMIN
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code = 'INVENTORY_ANALYTICS_REFRESH'
ON CONFLICT (role_id, permission_id) DO NOTHING;
