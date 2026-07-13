-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 84
-- File              : V84__seed_payroll_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed payroll permissions
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
-- V84__seed_payroll_permissions.sql
-- PLUS33 ERP — Enterprise Payroll Permissions Seed
-- ============================================================

INSERT INTO permissions (code, name, description) VALUES
('PAYROLL_VIEW', 'View Payroll', 'Allows viewing payroll runs, periods, and reports'),
('PAYROLL_PROCESS', 'Process Payroll', 'Allows calculating and initiating payroll runs'),
('PAYROLL_APPROVE', 'Approve Payroll', 'Allows approving calculated payroll runs'),
('PAYROLL_POST_GL', 'Post Payroll to GL', 'Allows posting payroll journal entries to General Ledger'),
('PAYROLL_PAY', 'Execute Payroll Payments', 'Allows executing bank disbursals for net salaries'),
('PAYROLL_REVERSAL', 'Reverse Payroll', 'Allows cancelling and reversing posted payroll runs'),
('PAYROLL_EXPORT', 'Export Payroll Data', 'Allows exporting payroll registers and bank files'),
('PAYROLL_REPORT_VIEW', 'View Payroll Dashboards', 'Allows viewing payroll analytics and materialized reporting views'),
('PAYROLL_ADMIN', 'Payroll Administration', 'Allows full administration of payroll rules and structures'),
('PAYROLL_CONFIGURATION', 'Payroll Configuration', 'Allows configuring payroll policies and salary components')
ON CONFLICT (code) DO NOTHING;

-- Map permissions to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN (
    'PAYROLL_VIEW', 'PAYROLL_PROCESS', 'PAYROLL_APPROVE', 'PAYROLL_POST_GL',
    'PAYROLL_PAY', 'PAYROLL_REVERSAL', 'PAYROLL_EXPORT', 'PAYROLL_REPORT_VIEW',
    'PAYROLL_ADMIN', 'PAYROLL_CONFIGURATION'
  )
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- Map permissions to FINANCE_MANAGER role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'FINANCE_MANAGER'
  AND p.code IN (
    'PAYROLL_VIEW', 'PAYROLL_PROCESS', 'PAYROLL_APPROVE', 'PAYROLL_POST_GL',
    'PAYROLL_PAY', 'PAYROLL_REPORT_VIEW', 'PAYROLL_EXPORT'
  )
ON CONFLICT (role_id, permission_id) DO NOTHING;
