-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 276
-- File              : V276__seed_v50_platform_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed v50 platform permissions
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
-- V276: Permissions
INSERT INTO permissions (code, name) VALUES
('scada:write', 'Authorize write registers commands with cryptographic signatures'),
('scada:alarm:manage', 'Acknowledge shelve and configure SCADA alarm thresholds'),
('iot:gateway:configure', 'Register active IoT edge gateways and verify certificate heartbeats')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('scada:write', 'scada:alarm:manage', 'iot:gateway:configure')
ON CONFLICT DO NOTHING;
