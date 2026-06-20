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
