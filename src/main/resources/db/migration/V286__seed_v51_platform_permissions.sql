-- V286: Permissions
INSERT INTO permissions (code, name) VALUES
('geofence:manage', 'Configure geofencing twin coordinates and polygon geometry rules'),
('geofence:query', 'Execute spatial-temporal queries bounding box searches'),
('deviancy:track', 'Monitor vehicle route path deviations and alerts logs')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('geofence:manage', 'geofence:query', 'deviancy:track')
ON CONFLICT DO NOTHING;
