-- V266: Permissions
INSERT INTO permissions (code, name) VALUES
('logistics:optimize', 'Execute supply chain and cost carbon route optimizations'),
('route:modify', 'Approve autonomous route changes rerouting plans modification'),
('delay:predict', 'Query transit ETA prediction status delay forecasts models')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('logistics:optimize', 'route:modify', 'delay:predict')
ON CONFLICT DO NOTHING;
