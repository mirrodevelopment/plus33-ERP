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
