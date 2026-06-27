-- V81__seed_tax_permissions.sql
-- PLUS33 ERP — Tax Management permissions and roles seed data

-- 1. Create permissions
INSERT INTO permissions (code, name, description)
VALUES
('TAX_RATE_MANAGE', 'Manage Tax Rates', 'Configure categories, rates, and groups'),
('TAX_RULE_MANAGE', 'Manage Tax Rules', 'Modify tax determination rules'),
('TAX_POSTING_PROFILE_MANAGE', 'Manage Tax Posting Profiles', 'Configure GL ledger mappings'),
('TAX_EXEMPTION_MANAGE', 'Manage Tax Exemptions', 'Approve exemption certificates'),
('TAX_OVERRIDE_APPROVE', 'Approve Tax Overrides', 'Approve tax override requests'),
('TAX_FILING_APPROVE', 'Approve Tax Filings', 'Approve periodic returns before submission'),
('TAX_PROVIDER_MANAGE', 'Manage Tax Providers', 'Setup localized Compliance connector adapters'),
('TAX_AUDIT_VIEW', 'View Tax Audit Logs', 'Read immutable audit trail log rows'),
('TAX_DASHBOARD_VIEW', 'View Tax Dashboard', 'Load dashboards and reports'),
('TAX_ADMIN', 'Tax Administration', 'Full administrative override permissions')
ON CONFLICT (code) DO NOTHING;

-- 2. Create roles
INSERT INTO roles (code, name, description)
VALUES
('TAX_MANAGER', 'Tax Manager', 'Tax operations and compliance management')
ON CONFLICT (code) DO NOTHING;

-- 3. Assign permissions to roles (ULTIMATE_ADMIN gets all, TAX_MANAGER gets all)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code IN ('ULTIMATE_ADMIN', 'TAX_MANAGER')
  AND p.code IN (
    'TAX_RATE_MANAGE',
    'TAX_RULE_MANAGE',
    'TAX_POSTING_PROFILE_MANAGE',
    'TAX_EXEMPTION_MANAGE',
    'TAX_OVERRIDE_APPROVE',
    'TAX_FILING_APPROVE',
    'TAX_PROVIDER_MANAGE',
    'TAX_AUDIT_VIEW',
    'TAX_DASHBOARD_VIEW',
    'TAX_ADMIN'
  )
ON CONFLICT (role_id, permission_id) DO NOTHING;
