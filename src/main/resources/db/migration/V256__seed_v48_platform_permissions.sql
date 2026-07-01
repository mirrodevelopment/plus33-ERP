-- V256: Permission Seeds
INSERT INTO permissions (code, name) VALUES
('graph:query', 'Execute semantic intelligence knowledge graph traversals'),
('causal:analyze', 'Perform causal reasoning root-cause analysis'),
('maintenance:predict', 'Query predictive maintenance forecasts model registry')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('graph:query', 'causal:analyze', 'maintenance:predict')
ON CONFLICT DO NOTHING;
