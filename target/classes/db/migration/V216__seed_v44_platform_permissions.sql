-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 216
-- File              : V216__seed_v44_platform_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed v44 platform permissions
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
-- V216: Seed V44 Platform Permissions
INSERT INTO permissions (code, name) VALUES
('platform:cache:purge', 'Purge and invalidate distributed caches namespaces and regions'),
('platform:scale:adjust', 'Adjust autoscaling policies, replicas limits, and thresholds'),
('platform:ha:switch', 'Initiate multi-region failover and switch routing replica nodes')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('platform:cache:purge', 'platform:scale:adjust', 'platform:ha:switch')
ON CONFLICT DO NOTHING;
