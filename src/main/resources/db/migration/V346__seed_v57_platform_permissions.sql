-- V346: Permissions
INSERT INTO permissions (code, name) VALUES
('dispatch:create', 'Create dynamic AI fleet dispatch tasks and driver schedules'),
('simulation:run', 'Execute route simulation runs for ETA and carbon checks'),
('load:balance', 'Audit and evaluate fleet dynamic load balancing allocations')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('dispatch:create', 'simulation:run', 'load:balance')
ON CONFLICT DO NOTHING;
