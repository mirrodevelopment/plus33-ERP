-- ============================================================
-- V49__seed_inventory_traceability_permissions.sql
-- PLUS33 ERP — Seeding inventory traceability permissions
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('INVENTORY_LOT_CREATE',     'Create Inventory Lot',      'Create new product batch/lots'),
('INVENTORY_LOT_VIEW',       'View Inventory Lot',        'View product batch/lots details'),
('INVENTORY_SERIAL_VIEW',    'View Inventory Serial',     'View unique serialized items'),
('INVENTORY_TRACE_VIEW',     'View Inventory Trace',      'View product, lot, and serial trace history'),
('INVENTORY_RECALL_CREATE',   'Create Inventory Recall',   'Initiate a product recall or quarantine workflow'),
('INVENTORY_RECALL_VIEW',     'View Inventory Recalls',    'View product recall logs')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code IN ('ULTIMATE_ADMIN', 'REGIONAL_MANAGER', 'WAREHOUSE_MANAGER')
  AND p.code IN (
    'INVENTORY_LOT_CREATE', 'INVENTORY_LOT_VIEW', 'INVENTORY_SERIAL_VIEW',
    'INVENTORY_TRACE_VIEW', 'INVENTORY_RECALL_CREATE', 'INVENTORY_RECALL_VIEW'
  )
ON CONFLICT (role_id, permission_id) DO NOTHING;
