-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 346
-- File              : V346__seed_v57_platform_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed v57 platform permissions
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
-- V346: Permissions
INSERT INTO permissions (code, name) VALUES
('dispatch:create', 'Create dynamic AI fleet dispatch tasks and driver schedules'),
('simulation:run', 'Execute route simulation runs for ETA and carbon checks'),
('load:balance', 'Audit and evaluate fleet dynamic load balancing allocations')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('dispatch:create', 'simulation:run', 'load:balance')
ON CONFLICT DO NOTHING;
