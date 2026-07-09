-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 72
-- File              : V72__financial_reporting_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : financial reporting permissions
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
-- V72__financial_reporting_permissions.sql
-- PLUS33 ERP — Financial Reporting Permissions
-- ============================================================

-- 1. Seed permissions
INSERT INTO permissions (code, name) VALUES
('FINANCIAL_REPORT_VIEW', 'View Trial Balance, Income Statement, Balance Sheet, and dashboards'),
('FINANCIAL_REPORT_EXPORT', 'Export financial reports to CSV and HTML formats'),
('PERIOD_LOCK_VIEW', 'View accounting period lock status'),
('PERIOD_LOCK_CREATE', 'Create or update accounting period locks (soft/hard)'),
('FISCAL_YEAR_CLOSE', 'Execute the fiscal year closing process'),
('FISCAL_YEAR_REOPEN', 'Reopen a previously closed fiscal year (highly restricted)')
ON CONFLICT (code) DO NOTHING;

-- 2. Map all permissions to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN ('FINANCIAL_REPORT_VIEW', 'FINANCIAL_REPORT_EXPORT', 'PERIOD_LOCK_VIEW', 'PERIOD_LOCK_CREATE', 'FISCAL_YEAR_CLOSE', 'FISCAL_YEAR_REOPEN')
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- 3. Map standard reporting and closing permissions to FINANCE_MANAGER role (excluding reopen)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'FINANCE_MANAGER'
  AND p.code IN ('FINANCIAL_REPORT_VIEW', 'FINANCIAL_REPORT_EXPORT', 'PERIOD_LOCK_VIEW', 'PERIOD_LOCK_CREATE', 'FISCAL_YEAR_CLOSE')
ON CONFLICT (role_id, permission_id) DO NOTHING;
