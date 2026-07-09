-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 316
-- File              : V316__seed_v54_platform_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed v54 platform permissions
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
-- V316: Permissions
INSERT INTO permissions (code, name) VALUES
('compliance:manage', 'Configure distributed edge device compliance policies rules'),
('compliance:audit', 'Trigger compliance evaluations audits and auto-remediations'),
('attestation:validate', 'Validate zero-trust device hardware TPM boot attestations')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('compliance:manage', 'compliance:audit', 'attestation:validate')
ON CONFLICT DO NOTHING;
