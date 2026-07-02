-- V376: Permissions
INSERT INTO permissions (code, name) VALUES
('esg:report', 'Generate enterprise fleet greenhouse gas ESG compliance report mapping outputs'),
('emissions:calculate', 'Compute ICE Scope 1 and EV charging Scope 2 emissions indices'),
('sustainability:audit', 'Perform ESG digital verification audits and carbon offsets tracking')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('esg:report', 'emissions:calculate', 'sustainability:audit')
ON CONFLICT DO NOTHING;
