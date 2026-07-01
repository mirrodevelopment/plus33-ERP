-- V236: Agent permissions
INSERT INTO permissions (code, name) VALUES
('agent:run', 'Invoke autonomous agents runtime cognitive cycles'),
('knowledge:manage', 'Manage vector knowledge base indexes and chunk ingestions'),
('workflow:execute', 'Trigger multi-agent workflows pipelines orchestration')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('agent:run', 'knowledge:manage', 'workflow:execute')
ON CONFLICT DO NOTHING;
