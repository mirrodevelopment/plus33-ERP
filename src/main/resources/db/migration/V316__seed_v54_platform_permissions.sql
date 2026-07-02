-- V316: Permissions
INSERT INTO permissions (code, name) VALUES
('compliance:manage', 'Configure distributed edge device compliance policies rules'),
('compliance:audit', 'Trigger compliance evaluations audits and auto-remediations'),
('attestation:validate', 'Validate zero-trust device hardware TPM boot attestations')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.code IN ('SYSTEM_ADMIN', 'GRC_ADMIN')
  AND p.code IN ('compliance:manage', 'compliance:audit', 'attestation:validate')
ON CONFLICT DO NOTHING;
