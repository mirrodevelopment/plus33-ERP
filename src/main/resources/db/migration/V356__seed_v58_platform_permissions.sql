-- V356: Permissions
INSERT INTO permissions (code, name) VALUES
('fuel:optimize', 'Configure fleet dynamic fuel efficiency optimization policies and strategies'),
('driving:audit', 'Trigger eco-driving behavior diagnostics and driver score audits'),
('eco:advisor', 'Query fuel efficiency advisor suggestions and emissions mitigations')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('fuel:optimize', 'driving:audit', 'eco:advisor')
ON CONFLICT DO NOTHING;
