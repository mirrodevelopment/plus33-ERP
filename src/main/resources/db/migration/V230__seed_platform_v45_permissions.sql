-- V230: Seed V45 Platform Permissions
INSERT INTO permissions (code, name) VALUES
('platform:aiops:run', 'Execute AIOps trend projections and anomalies predictions models'),
('platform:finops:report', 'Review cloud resource cost chargebacks and cost optimizations reports'),
('platform:policy:audit', 'Review Open Policy Agent dynamic rules access decision audits history')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('platform:aiops:run', 'platform:finops:report', 'platform:policy:audit')
ON CONFLICT DO NOTHING;
