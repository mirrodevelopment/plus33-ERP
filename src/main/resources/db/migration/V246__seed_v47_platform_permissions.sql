-- V246: Permissions
INSERT INTO permissions (code, name) VALUES
('process:mine', 'Execute enterprise process mining analysis variant discovery'),
('twin:control', 'Execute telemetry state projections and simulation twins commands'),
('action:approve', 'Approve low-confidence autonomous actions decisions proposals')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('process:mine', 'twin:control', 'action:approve')
ON CONFLICT DO NOTHING;
