-- V276: Permissions
INSERT INTO permissions (code, name) VALUES
('scada:write', 'Authorize write registers commands with cryptographic signatures'),
('scada:alarm:manage', 'Acknowledge shelve and configure SCADA alarm thresholds'),
('iot:gateway:configure', 'Register active IoT edge gateways and verify certificate heartbeats')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('scada:write', 'scada:alarm:manage', 'iot:gateway:configure')
ON CONFLICT DO NOTHING;
