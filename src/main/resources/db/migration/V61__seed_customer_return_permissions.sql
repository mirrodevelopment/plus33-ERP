-- ============================================================
-- V61__seed_customer_return_permissions.sql
-- PLUS33 ERP — Customer Returns & Credit Notes Permissions
-- ============================================================

-- 1. Seed Permissions
INSERT INTO permissions (code, name, description)
VALUES
('CUSTOMER_RETURN_CREATE',   'Create Customer Returns',   'Initiate customer return requests'),
('CUSTOMER_RETURN_VIEW',     'View Customer Returns',     'View return details and items'),
('CUSTOMER_RETURN_UPDATE',   'Update Customer Returns',   'Modify draft return requests'),
('CUSTOMER_RETURN_APPROVE',  'Approve Customer Returns',  'Approve return requests'),
('CUSTOMER_RETURN_RECEIVE',  'Receive Returned Items',    'Log actual arrival of returned goods'),
('CUSTOMER_RETURN_INSPECT',  'Inspect Returned Items',    'Conduct quality inspections and decide stock outcomes'),
('CUSTOMER_RETURN_CLOSE',    'Close Customer Returns',    'Finalize return lifecycle and approve/post credit notes'),
('CUSTOMER_RETURN_CANCEL',   'Cancel Customer Returns',   'Cancel return requests and related credit notes'),
('CREDIT_NOTE_VIEW',         'View Credit Notes',         'View generated credit notes and lines');

-- 2. Assign to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN (
    'CUSTOMER_RETURN_CREATE', 'CUSTOMER_RETURN_VIEW', 'CUSTOMER_RETURN_UPDATE',
    'CUSTOMER_RETURN_APPROVE', 'CUSTOMER_RETURN_RECEIVE', 'CUSTOMER_RETURN_INSPECT',
    'CUSTOMER_RETURN_CLOSE', 'CUSTOMER_RETURN_CANCEL', 'CREDIT_NOTE_VIEW'
  );
