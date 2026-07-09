-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 74
-- File              : V74__fixed_asset_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : fixed asset permissions
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
-- V74__fixed_asset_permissions.sql
-- PLUS33 ERP — Fixed Asset Management Permissions
-- ============================================================

-- 1. Seed permissions
INSERT INTO permissions (code, name) VALUES
('FIXED_ASSET_VIEW', 'View fixed assets, categories, assignments, audits, and dashboards'),
('FIXED_ASSET_CREATE', 'Create or draft new fixed assets and categories'),
('FIXED_ASSET_ACQUIRE', 'Acquire/capitalize assets and post initial GL journal entries'),
('FIXED_ASSET_DEPRECIATE', 'Run depreciation, dry-runs, and view depreciation logs'),
('FIXED_ASSET_TRANSFER', 'Transfer assets between warehouses or stores'),
('FIXED_ASSET_DISPOSE', 'Dispose, sell, or write off assets and post GL adjustments'),
('FIXED_ASSET_MAINTAIN', 'Log maintenance events and optionally capitalize maintenance costs'),
('FIXED_ASSET_ASSIGN', 'Assign assets to employees, departments, or locations'),
('FIXED_ASSET_AUDIT', 'Create and submit physical verification audits')
ON CONFLICT (code) DO NOTHING;

-- 2. Map all permissions to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN (
    'FIXED_ASSET_VIEW', 'FIXED_ASSET_CREATE', 'FIXED_ASSET_ACQUIRE',
    'FIXED_ASSET_DEPRECIATE', 'FIXED_ASSET_TRANSFER', 'FIXED_ASSET_DISPOSE',
    'FIXED_ASSET_MAINTAIN', 'FIXED_ASSET_ASSIGN', 'FIXED_ASSET_AUDIT'
  )
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- 3. Map all permissions to FINANCE_MANAGER role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'FINANCE_MANAGER'
  AND p.code IN (
    'FIXED_ASSET_VIEW', 'FIXED_ASSET_CREATE', 'FIXED_ASSET_ACQUIRE',
    'FIXED_ASSET_DEPRECIATE', 'FIXED_ASSET_TRANSFER', 'FIXED_ASSET_DISPOSE',
    'FIXED_ASSET_MAINTAIN', 'FIXED_ASSET_ASSIGN', 'FIXED_ASSET_AUDIT'
  )
ON CONFLICT (role_id, permission_id) DO NOTHING;
