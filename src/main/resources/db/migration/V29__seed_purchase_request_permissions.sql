-- ============================================================
-- V29__seed_purchase_request_permissions.sql
-- PLUS33 ERP — Purchase Request Management Permissions Seeding
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('PURCHASE_REQUEST_CREATE',   'Create Purchase Requests',    'Create new purchase requests'),
('PURCHASE_REQUEST_VIEW',     'View Purchase Requests',      'View purchase requests and details'),
('PURCHASE_REQUEST_UPDATE',   'Update Purchase Requests',    'Modify purchase requests in DRAFT state'),
('PURCHASE_REQUEST_SUBMIT',   'Submit Purchase Requests',    'Submit draft purchase requests for approval'),
('PURCHASE_REQUEST_APPROVE',  'Approve Purchase Requests',   'Approve submitted purchase requests'),
('PURCHASE_REQUEST_REJECT',   'Reject Purchase Requests',    'Reject submitted purchase requests'),
('PURCHASE_REQUEST_CANCEL',   'Cancel Purchase Requests',    'Cancel submitted purchase requests'),
('PURCHASE_REQUEST_CONVERT',  'Convert Purchase Requests',   'Convert approved purchase requests to purchase orders');

-- Assign to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN (
    'PURCHASE_REQUEST_CREATE', 'PURCHASE_REQUEST_VIEW', 'PURCHASE_REQUEST_UPDATE',
    'PURCHASE_REQUEST_SUBMIT', 'PURCHASE_REQUEST_APPROVE', 'PURCHASE_REQUEST_REJECT',
    'PURCHASE_REQUEST_CANCEL', 'PURCHASE_REQUEST_CONVERT'
  );
