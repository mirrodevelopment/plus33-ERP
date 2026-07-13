-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 23
-- File              : V23__seed_product_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed product permissions
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
-- V23__seed_product_permissions.sql
-- PLUS33 ERP — Product Management Permissions Seeding
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('PRODUCT_CREATE', 'Create Products', 'Create new catalog products'),
('PRODUCT_VIEW',   'View Products',   'View product information'),
('PRODUCT_UPDATE', 'Update Products', 'Modify product details'),
('PRODUCT_DELETE', 'Delete Products', 'Soft-delete products');

-- Assign to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN ('PRODUCT_CREATE', 'PRODUCT_VIEW', 'PRODUCT_UPDATE', 'PRODUCT_DELETE');
