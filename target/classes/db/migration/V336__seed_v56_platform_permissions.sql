-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 336
-- File              : V336__seed_v56_platform_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed v56 platform permissions
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
-- V336: Permissions
INSERT INTO permissions (code, name) VALUES
('routing:optimize', 'Configure fleet dynamic routing optimization policies and constraints'),
('emissions:audit', 'Trigger carbon footprint diagnostics reports and fuel logs audits'),
('cost:query', 'Query route operational cost metrics analysis and toll configurations')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('routing:optimize', 'emissions:audit', 'cost:query')
ON CONFLICT DO NOTHING;
