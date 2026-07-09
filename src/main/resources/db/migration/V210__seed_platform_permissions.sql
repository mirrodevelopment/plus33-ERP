-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 210
-- File              : V210__seed_platform_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed platform permissions
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
-- V210: Seed Platform Permissions
INSERT INTO permissions (code, name) VALUES
('platform:write', 'Modify platform configurations, secrets, and maintenance windows'),
('platform:read', 'View platform health, metrics, nodes, and configurations'),
('platform:deploy', 'Orchestrate environment Blue/Green switches and canary weights'),
('platform:backup:restore', 'Initiate backup restorations and integrity sandbox verification')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('platform:write', 'platform:read', 'platform:deploy', 'platform:backup:restore')
ON CONFLICT DO NOTHING;
