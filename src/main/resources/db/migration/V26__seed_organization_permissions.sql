-- ============================================================
-- V26__seed_organization_permissions.sql
-- PLUS33 ERP — Organization Management Permissions Seeding
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('COMPANY_VIEW',      'View Companies',      'View company details'),
('COMPANY_UPDATE',    'Update Companies',    'Modify company details'),

('REGION_CREATE',     'Create Regions',      'Create new regions'),
('REGION_VIEW',       'View Regions',        'View regional information'),
('REGION_UPDATE',     'Update Regions',      'Modify regional details'),
('REGION_DELETE',     'Delete Regions',      'Soft-delete regions'),

('WAREHOUSE_CREATE',  'Create Warehouses',   'Create new warehouses'),
('WAREHOUSE_VIEW',    'View Warehouses',     'View warehouse information'),
('WAREHOUSE_UPDATE',  'Update Warehouses',   'Modify warehouse details'),
('WAREHOUSE_DELETE',  'Delete Warehouses',   'Soft-delete warehouses'),

('STORE_CREATE',      'Create Stores',       'Create new stores'),
('STORE_VIEW',        'View Stores',         'View store information'),
('STORE_UPDATE',      'Update Stores',       'Modify store details'),
('STORE_DELETE',      'Delete Stores',       'Soft-delete stores');

-- Assign to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN (
    'COMPANY_VIEW', 'COMPANY_UPDATE',
    'REGION_CREATE', 'REGION_VIEW', 'REGION_UPDATE', 'REGION_DELETE',
    'WAREHOUSE_CREATE', 'WAREHOUSE_VIEW', 'WAREHOUSE_UPDATE', 'WAREHOUSE_DELETE',
    'STORE_CREATE', 'STORE_VIEW', 'STORE_UPDATE', 'STORE_DELETE'
  );
