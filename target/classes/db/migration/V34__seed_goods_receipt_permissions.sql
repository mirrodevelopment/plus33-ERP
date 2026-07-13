-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 34
-- File              : V34__seed_goods_receipt_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed goods receipt permissions
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
-- V34__seed_goods_receipt_permissions.sql
-- PLUS33 ERP — Goods Receipt Management Permissions Seeding
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('GOODS_RECEIPT_CREATE',   'Create Goods Receipts',    'Record goods receipts'),
('GOODS_RECEIPT_VIEW',     'View Goods Receipts',      'View goods receipts and details'),
('GOODS_RECEIPT_UPDATE',   'Update Goods Receipts',    'Modify remarks or supplier references on goods receipts'),
('GOODS_RECEIPT_CANCEL',   'Cancel Goods Receipts',    'Cancel and reverse goods receipts');

-- Assign to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN (
    'GOODS_RECEIPT_CREATE', 'GOODS_RECEIPT_VIEW', 'GOODS_RECEIPT_UPDATE', 'GOODS_RECEIPT_CANCEL'
  );
