-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 376
-- File              : V376__seed_v60_platform_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed v60 platform permissions
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
-- V376: Permissions
INSERT INTO permissions (code, name) VALUES
('esg:report', 'Generate enterprise fleet greenhouse gas ESG compliance report mapping outputs'),
('emissions:calculate', 'Compute ICE Scope 1 and EV charging Scope 2 emissions indices'),
('sustainability:audit', 'Perform ESG digital verification audits and carbon offsets tracking')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('esg:report', 'emissions:calculate', 'sustainability:audit')
ON CONFLICT DO NOTHING;
