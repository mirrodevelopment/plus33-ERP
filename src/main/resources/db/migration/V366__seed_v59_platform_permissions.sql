-- V366: Permissions
INSERT INTO permissions (code, name) VALUES
('ev:manage', 'Configure EV energy management and state of charge limits'),
('battery:diagnose', 'Execute battery state of health diagnostics and life analysis'),
('charging:schedule', 'Manage EV charging station reservations and schedules')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('ev:manage', 'battery:diagnose', 'charging:schedule')
ON CONFLICT DO NOTHING;
