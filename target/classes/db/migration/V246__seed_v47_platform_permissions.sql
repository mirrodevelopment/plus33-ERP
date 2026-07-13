-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 246
-- File              : V246__seed_v47_platform_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed v47 platform permissions
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
-- V246: Permissions
INSERT INTO permissions (code, name) VALUES
('process:mine', 'Execute enterprise process mining analysis variant discovery'),
('twin:control', 'Execute telemetry state projections and simulation twins commands'),
('action:approve', 'Approve low-confidence autonomous actions decisions proposals')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('process:mine', 'twin:control', 'action:approve')
ON CONFLICT DO NOTHING;
