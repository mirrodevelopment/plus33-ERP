-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 67
-- File              : V67__seed_ap_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed ap permissions
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
-- V67__seed_ap_permissions.sql
-- PLUS33 ERP — Accounts Payable Permissions
-- ============================================================

-- 1. Seed permissions
INSERT INTO permissions (code, name) VALUES
('AP_VIEW', 'View Accounts Payable reports, aging, and dashboard metrics'),
('AP_STATEMENT_VIEW', 'View supplier chronological AP statements and ledgers'),
('VENDOR_BILL_VOID', 'Void draft or submitted supplier invoices')
ON CONFLICT (code) DO NOTHING;

-- 2. Map permissions to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN ('AP_VIEW', 'AP_STATEMENT_VIEW', 'VENDOR_BILL_VOID')
ON CONFLICT (role_id, permission_id) DO NOTHING;
