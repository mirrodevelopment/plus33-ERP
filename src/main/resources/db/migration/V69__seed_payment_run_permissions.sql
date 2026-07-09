-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 69
-- File              : V69__seed_payment_run_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed payment run permissions
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
-- V69__seed_payment_run_permissions.sql
-- PLUS33 ERP — Payment Run Permissions
-- ============================================================

-- 1. Seed permissions
INSERT INTO permissions (code, name) VALUES
('PAYMENT_RUN_CREATE', 'Create and modify draft and calculated payment runs'),
('PAYMENT_RUN_VIEW', 'View payment runs and operational dashboard metrics'),
('PAYMENT_RUN_APPROVE', 'Approve calculated payment runs and lock them'),
('PAYMENT_RUN_EXECUTE', 'Execute approved payment runs and generate bank exports')
ON CONFLICT (code) DO NOTHING;

-- 2. Map permissions to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN ('PAYMENT_RUN_CREATE', 'PAYMENT_RUN_VIEW', 'PAYMENT_RUN_APPROVE', 'PAYMENT_RUN_EXECUTE')
ON CONFLICT (role_id, permission_id) DO NOTHING;
