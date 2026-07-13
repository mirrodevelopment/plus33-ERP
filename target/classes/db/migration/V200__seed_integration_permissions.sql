-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 200
-- File              : V200__seed_integration_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed integration permissions
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
-- V200: Seed Integration Permissions DDL & Data
INSERT INTO permissions (code, name) VALUES
('integration:write', 'Create and modify integration artifacts'),
('integration:read', 'View integration data and logs'),
('gateway:bypass', 'Bypass Gateway limits and rate limiting check'),
('workflow:orchestrate', 'Execute and monitor workflows')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('integration:write', 'integration:read', 'gateway:bypass', 'workflow:orchestrate')
ON CONFLICT DO NOTHING;
