-- ============================================================
-- V55__seed_sales_order_permissions.sql
-- PLUS33 ERP — Sales Order Permissions Seeding
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('SALES_ORDER_CREATE', 'Create Sales Orders', 'Allows creating new sales orders'),
('SALES_ORDER_VIEW',   'View Sales Orders',   'Allows viewing sales order information'),
('SALES_ORDER_UPDATE', 'Update Sales Orders', 'Allows modifying draft sales orders'),
('SALES_ORDER_SUBMIT', 'Submit Sales Orders', 'Allows submitting sales orders for approval'),
('SALES_ORDER_APPROVE', 'Approve Sales Orders', 'Allows approving submitted sales orders'),
('SALES_ORDER_CANCEL', 'Cancel Sales Orders', 'Allows cancelling sales orders'),
('SALES_ORDER_OVERRIDE_CREDIT_LIMIT', 'Override Sales Order Credit Limit', 'Allows overriding credit limit check during sales order approval')
ON CONFLICT (code) DO NOTHING;

-- Map general sales order permissions to ULTIMATE_ADMIN
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN (
    'SALES_ORDER_CREATE', 'SALES_ORDER_VIEW', 'SALES_ORDER_UPDATE', 
    'SALES_ORDER_SUBMIT', 'SALES_ORDER_APPROVE', 'SALES_ORDER_CANCEL'
  )
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- Map override permissions to senior finance and management roles (ULTIMATE_ADMIN, REGIONAL_MANAGER, FINANCE_MANAGER)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code IN ('ULTIMATE_ADMIN', 'REGIONAL_MANAGER', 'FINANCE_MANAGER')
  AND p.code IN ('SALES_ORDER_OVERRIDE_CREDIT_LIMIT', 'CUSTOMER_OVERRIDE_CREDIT_LIMIT')
ON CONFLICT (role_id, permission_id) DO NOTHING;
