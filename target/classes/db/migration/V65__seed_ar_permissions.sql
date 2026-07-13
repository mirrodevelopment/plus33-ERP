-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 65
-- File              : V65__seed_ar_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed ar permissions
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
-- V65__seed_ar_permissions.sql
-- PLUS33 ERP — Accounts Receivable Permissions
-- ============================================================

-- 1. Seed permissions
INSERT INTO permissions (code, name, description)
VALUES
('AR_VIEW',               'View AR Reports',           'Access aging reports, AR summary, customer balance, and overdue invoice lists'),
('AR_STATEMENT_VIEW',     'View Customer Statements',  'Access chronological customer AR statements with running balance'),
('AR_WRITE_OFF_CREATE',   'Create AR Write-offs',      'Record bad-debt write-offs and post journal entries against Accounts Receivable');

-- 2. Assign all to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM   roles r
CROSS JOIN permissions p
WHERE  r.code = 'ULTIMATE_ADMIN'
  AND  p.code IN ('AR_VIEW', 'AR_STATEMENT_VIEW', 'AR_WRITE_OFF_CREATE');
