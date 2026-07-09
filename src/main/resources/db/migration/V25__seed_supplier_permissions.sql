-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 25
-- File              : V25__seed_supplier_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed supplier permissions
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
-- V25__seed_supplier_permissions.sql
-- PLUS33 ERP — Supplier Management Permissions Seeding
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('SUPPLIER_CREATE', 'Create Suppliers', 'Create new suppliers'),
('SUPPLIER_VIEW',   'View Suppliers',   'View supplier information'),
('SUPPLIER_UPDATE', 'Update Suppliers', 'Modify supplier details'),
('SUPPLIER_DELETE', 'Delete Suppliers', 'Soft-delete suppliers');

-- Assign all four permissions to ULTIMATE_ADMIN role mapping
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN ('SUPPLIER_CREATE', 'SUPPLIER_VIEW', 'SUPPLIER_UPDATE', 'SUPPLIER_DELETE');
