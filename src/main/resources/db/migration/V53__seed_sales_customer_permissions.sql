-- ============================================================
-- V53__seed_sales_customer_permissions.sql
-- PLUS33 ERP — Sales Customer Permissions Seeding
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('CUSTOMER_CREATE', 'Create Customers', 'Allows creating new customer records'),
('CUSTOMER_VIEW',   'View Customers',   'Allows viewing customer information'),
('CUSTOMER_UPDATE', 'Update Customers', 'Allows modifying customer details'),
('CUSTOMER_DELETE', 'Delete Customers', 'Allows soft-deleting customer records'),
('CUSTOMER_ACTIVATE', 'Activate Customers', 'Allows activating suspended/inactive customers'),
('CUSTOMER_DEACTIVATE', 'Deactivate Customers', 'Allows deactivating customers'),
('CUSTOMER_OVERRIDE_CREDIT_LIMIT', 'Override Credit Limits', 'Allows overriding credit validations on customer level'),
('SALES_OVERRIDE_CREDIT_LIMIT', 'Override Sales Credit Limits', 'Allows overriding credit validations during sales order approval')
ON CONFLICT (code) DO NOTHING;

-- Map permissions to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN (
    'CUSTOMER_CREATE', 'CUSTOMER_VIEW', 'CUSTOMER_UPDATE', 'CUSTOMER_DELETE',
    'CUSTOMER_ACTIVATE', 'CUSTOMER_DEACTIVATE', 'CUSTOMER_OVERRIDE_CREDIT_LIMIT', 'SALES_OVERRIDE_CREDIT_LIMIT'
  )
ON CONFLICT (role_id, permission_id) DO NOTHING;
