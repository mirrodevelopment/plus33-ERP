-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 296
-- File              : V296__seed_v52_platform_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed v52 platform permissions
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
-- V296: Permissions
INSERT INTO permissions (code, name) VALUES
('edge:configure', 'Configure distributed edge nodes registry settings'),
('edge:sync', 'Trigger store-and-forward edge synchronization reconciliation'),
('edge:monitor', 'Monitor health metrics logs and packet loss rates of edge nodes')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('edge:configure', 'edge:sync', 'edge:monitor')
ON CONFLICT DO NOTHING;
