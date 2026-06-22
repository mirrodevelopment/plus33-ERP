-- ============================================================
-- V45__seed_stock_count_permissions.sql
-- PLUS33 ERP — Stock Count Permissions Seed Data
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('STOCK_COUNT_CREATE',   'Create Stock Count',   'Create new stock count sessions'),
('STOCK_COUNT_VIEW',     'View Stock Count',     'View stock count headers and details'),
('STOCK_COUNT_UPDATE',   'Update Stock Count',   'Update details on draft stock counts'),
('STOCK_COUNT_ASSIGN',   'Assign Stock Count',   'Assign stock counts to employees'),
('STOCK_COUNT_SUBMIT',   'Submit Stock Count',   'Submit completed physical counts'),
('STOCK_COUNT_APPROVE',  'Approve Stock Count',  'Approve or reject submitted stock counts'),
('STOCK_COUNT_POST',     'Post Stock Count',     'Post approved stock counts and create inventory adjustments')
ON CONFLICT (code) DO NOTHING;

-- Assign permissions to ULTIMATE_ADMIN, REGIONAL_MANAGER, and WAREHOUSE_MANAGER roles
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code IN ('ULTIMATE_ADMIN', 'REGIONAL_MANAGER', 'WAREHOUSE_MANAGER')
  AND p.code IN (
    'STOCK_COUNT_CREATE', 'STOCK_COUNT_VIEW', 'STOCK_COUNT_UPDATE',
    'STOCK_COUNT_ASSIGN', 'STOCK_COUNT_SUBMIT', 'STOCK_COUNT_APPROVE',
    'STOCK_COUNT_POST'
  )
ON CONFLICT (role_id, permission_id) DO NOTHING;
