-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 230
-- File              : V230__seed_platform_v45_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed platform v45 permissions
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
-- V230: Seed V45 Platform Permissions
INSERT INTO permissions (code, name) VALUES
('platform:aiops:run', 'Execute AIOps trend projections and anomalies predictions models'),
('platform:finops:report', 'Review cloud resource cost chargebacks and cost optimizations reports'),
('platform:policy:audit', 'Review Open Policy Agent dynamic rules access decision audits history')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('platform:aiops:run', 'platform:finops:report', 'platform:policy:audit')
ON CONFLICT DO NOTHING;
