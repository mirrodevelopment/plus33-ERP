-- V210: Seed Platform Permissions
INSERT INTO permissions (code, name) VALUES
('platform:write', 'Modify platform configurations, secrets, and maintenance windows'),
('platform:read', 'View platform health, metrics, nodes, and configurations'),
('platform:deploy', 'Orchestrate environment Blue/Green switches and canary weights'),
('platform:backup:restore', 'Initiate backup restorations and integrity sandbox verification')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('platform:write', 'platform:read', 'platform:deploy', 'platform:backup:restore')
ON CONFLICT DO NOTHING;
