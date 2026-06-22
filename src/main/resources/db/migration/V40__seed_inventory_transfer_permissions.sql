-- ============================================================
-- V40__seed_inventory_transfer_permissions.sql
-- PLUS33 ERP — Inventory Transfer Permissions Seed Data
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('INVENTORY_TRANSFER_CREATE',   'Create Inventory Transfer',   'Create and request new internal stock transfers'),
('INVENTORY_TRANSFER_VIEW',     'View Inventory Transfer',     'View inventory transfer headers and details'),
('INVENTORY_TRANSFER_UPDATE',   'Update Inventory Transfer',   'Update details on draft inventory transfers'),
('INVENTORY_TRANSFER_APPROVE',  'Approve Inventory Transfer',  'Approve inventory transfers and reserve stock'),
('INVENTORY_TRANSFER_DISPATCH', 'Dispatch Inventory Transfer', 'Dispatch approved stock transfers and decrease source inventory'),
('INVENTORY_TRANSFER_RECEIVE',  'Receive Inventory Transfer',  'Receive dispatched transfers and increase destination inventory'),
('INVENTORY_TRANSFER_CANCEL',   'Cancel Inventory Transfer',   'Cancel inventory transfers and release reserved stock')
ON CONFLICT (code) DO NOTHING;

-- Assign permissions to ULTIMATE_ADMIN, REGIONAL_MANAGER, and WAREHOUSE_MANAGER roles
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code IN ('ULTIMATE_ADMIN', 'REGIONAL_MANAGER', 'WAREHOUSE_MANAGER')
  AND p.code IN (
    'INVENTORY_TRANSFER_CREATE', 'INVENTORY_TRANSFER_VIEW', 'INVENTORY_TRANSFER_UPDATE',
    'INVENTORY_TRANSFER_APPROVE', 'INVENTORY_TRANSFER_DISPATCH', 'INVENTORY_TRANSFER_RECEIVE',
    'INVENTORY_TRANSFER_CANCEL'
  )
ON CONFLICT (role_id, permission_id) DO NOTHING;
