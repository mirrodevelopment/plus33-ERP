-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 31
-- File              : V31__seed_purchase_order_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed purchase order permissions
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
-- V31__seed_purchase_order_permissions.sql
-- PLUS33 ERP — Purchase Order Management Permissions Seeding
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('PURCHASE_ORDER_CREATE',   'Create Purchase Orders',    'Create new purchase orders'),
('PURCHASE_ORDER_VIEW',     'View Purchase Orders',      'View purchase orders and details'),
('PURCHASE_ORDER_UPDATE',   'Update Purchase Orders',    'Modify purchase orders in DRAFT state'),
('PURCHASE_ORDER_APPROVE',  'Approve/Issue Purchase Orders',   'Approve and issue purchase orders'),
('PURCHASE_ORDER_CANCEL',   'Cancel Purchase Orders',    'Cancel issued purchase orders'),
('PURCHASE_ORDER_CLOSE',    'Close Purchase Orders',     'Close fully received purchase orders');

-- Assign to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN (
    'PURCHASE_ORDER_CREATE', 'PURCHASE_ORDER_VIEW', 'PURCHASE_ORDER_UPDATE',
    'PURCHASE_ORDER_APPROVE', 'PURCHASE_ORDER_CANCEL', 'PURCHASE_ORDER_CLOSE'
  );
