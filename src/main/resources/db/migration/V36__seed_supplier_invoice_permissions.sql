-- ============================================================
-- V36__seed_supplier_invoice_permissions.sql
-- PLUS33 ERP — Supplier Invoice Management Permissions Seeding
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('SUPPLIER_INVOICE_CREATE',   'Create Supplier Invoices',    'Create and submit new supplier invoices'),
('SUPPLIER_INVOICE_VIEW',     'View Supplier Invoices',      'View supplier invoices and item details'),
('SUPPLIER_INVOICE_UPDATE',   'Update Supplier Invoices',    'Modify remarks or references on draft invoices'),
('SUPPLIER_INVOICE_APPROVE',  'Approve Supplier Invoices',   'Approve supplier invoices and post journal entries'),
('SUPPLIER_INVOICE_CANCEL',   'Cancel Supplier Invoices',    'Cancel supplier invoices and reverse journal entries');

-- Assign to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN (
    'SUPPLIER_INVOICE_CREATE', 'SUPPLIER_INVOICE_VIEW', 'SUPPLIER_INVOICE_UPDATE', 'SUPPLIER_INVOICE_APPROVE', 'SUPPLIER_INVOICE_CANCEL'
  );
