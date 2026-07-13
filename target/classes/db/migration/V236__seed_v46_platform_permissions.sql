-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 236
-- File              : V236__seed_v46_platform_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed v46 platform permissions
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
-- V236: Agent permissions
INSERT INTO permissions (code, name) VALUES
('agent:run', 'Invoke autonomous agents runtime cognitive cycles'),
('knowledge:manage', 'Manage vector knowledge base indexes and chunk ingestions'),
('workflow:execute', 'Trigger multi-agent workflows pipelines orchestration')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('agent:run', 'knowledge:manage', 'workflow:execute')
ON CONFLICT DO NOTHING;
