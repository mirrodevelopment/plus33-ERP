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
