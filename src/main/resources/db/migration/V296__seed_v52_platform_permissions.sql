-- V296: Permissions
INSERT INTO permissions (code, name) VALUES
('edge:configure', 'Configure distributed edge nodes registry settings'),
('edge:sync', 'Trigger store-and-forward edge synchronization reconciliation'),
('edge:monitor', 'Monitor health metrics logs and packet loss rates of edge nodes')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('edge:configure', 'edge:sync', 'edge:monitor')
ON CONFLICT DO NOTHING;
