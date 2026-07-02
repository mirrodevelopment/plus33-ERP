-- V336: Permissions
INSERT INTO permissions (code, name) VALUES
('routing:optimize', 'Configure fleet dynamic routing optimization policies and constraints'),
('emissions:audit', 'Trigger carbon footprint diagnostics reports and fuel logs audits'),
('cost:query', 'Query route operational cost metrics analysis and toll configurations')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('routing:optimize', 'emissions:audit', 'cost:query')
ON CONFLICT DO NOTHING;
