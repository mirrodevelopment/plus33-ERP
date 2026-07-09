-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 286
-- File              : V286__seed_v51_platform_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed v51 platform permissions
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
-- V286: Permissions
INSERT INTO permissions (code, name) VALUES
('geofence:manage', 'Configure geofencing twin coordinates and polygon geometry rules'),
('geofence:query', 'Execute spatial-temporal queries bounding box searches'),
('deviancy:track', 'Monitor vehicle route path deviations and alerts logs')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('geofence:manage', 'geofence:query', 'deviancy:track')
ON CONFLICT DO NOTHING;
