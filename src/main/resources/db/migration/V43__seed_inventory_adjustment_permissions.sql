-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 43
-- File              : V43__seed_inventory_adjustment_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed inventory adjustment permissions
--
-- Tables Created    : N/A
-- Tables Altered    : N/A
-- Seed Data For     : permissions, role_permissions
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V43__seed_inventory_adjustment_permissions.sql
-- PLUS33 ERP — Inventory Adjustment Permissions Seed Data
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('INVENTORY_ADJUSTMENT_CREATE',   'Create Inventory Adjustment',   'Create new inventory stock adjustments'),
('INVENTORY_ADJUSTMENT_VIEW',     'View Inventory Adjustment',     'View inventory adjustment headers and details'),
('INVENTORY_ADJUSTMENT_UPDATE',   'Update Inventory Adjustment',   'Update details on draft inventory adjustments'),
('INVENTORY_ADJUSTMENT_APPROVE',  'Approve Inventory Adjustment',  'Approve submitted inventory adjustments'),
('INVENTORY_ADJUSTMENT_POST',     'Post Inventory Adjustment',     'Post approved inventory adjustments and update physical stock levels'),
('INVENTORY_ADJUSTMENT_CANCEL',   'Cancel Inventory Adjustment',   'Cancel inventory adjustments')
ON CONFLICT (code) DO NOTHING;

-- Assign permissions to ULTIMATE_ADMIN, REGIONAL_MANAGER, and WAREHOUSE_MANAGER roles
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code IN ('ULTIMATE_ADMIN', 'REGIONAL_MANAGER', 'WAREHOUSE_MANAGER')
  AND p.code IN (
    'INVENTORY_ADJUSTMENT_CREATE', 'INVENTORY_ADJUSTMENT_VIEW', 'INVENTORY_ADJUSTMENT_UPDATE',
    'INVENTORY_ADJUSTMENT_APPROVE', 'INVENTORY_ADJUSTMENT_POST', 'INVENTORY_ADJUSTMENT_CANCEL'
  )
ON CONFLICT (role_id, permission_id) DO NOTHING;
