-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 266
-- File              : V266__seed_v49_platform_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed v49 platform permissions
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
-- V266: Permissions
INSERT INTO permissions (code, name) VALUES
('logistics:optimize', 'Execute supply chain and cost carbon route optimizations'),
('route:modify', 'Approve autonomous route changes rerouting plans modification'),
('delay:predict', 'Query transit ETA prediction status delay forecasts models')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('logistics:optimize', 'route:modify', 'delay:predict')
ON CONFLICT DO NOTHING;
