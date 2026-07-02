-- V326: Permissions
INSERT INTO permissions (code, name) VALUES
('predictive:configure', 'Configure predictive models remaining useful life health policies'),
('predictive:query', 'Execute prognostics runs and query predicted failures windows'),
('reliability:audit', 'Query reliability engineering metrics MTBF and MTTR failures logs')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('predictive:configure', 'predictive:query', 'reliability:audit')
ON CONFLICT DO NOTHING;
