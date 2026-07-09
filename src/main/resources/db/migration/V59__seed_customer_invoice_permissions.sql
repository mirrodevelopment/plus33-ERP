-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 59
-- File              : V59__seed_customer_invoice_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed customer invoice permissions
--
-- Tables Created    : N/A
-- Tables Altered    : N/A
-- Seed Data For     : permissions, role_permissions
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V59__seed_customer_invoice_permissions.sql
-- PLUS33 ERP — Customer Invoice Management Permissions Seeding
-- ============================================================

INSERT INTO permissions (code, name, description)
VALUES
('CUSTOMER_INVOICE_CREATE',   'Create Customer Invoices',    'Create new customer invoices in DRAFT status'),
('CUSTOMER_INVOICE_VIEW',     'View Customer Invoices',      'View customer invoices and item details'),
('CUSTOMER_INVOICE_UPDATE',   'Update Customer Invoices',    'Modify draft customer invoices'),
('CUSTOMER_INVOICE_SUBMIT',   'Submit Customer Invoices',    'Submit draft customer invoices for approval'),
('CUSTOMER_INVOICE_APPROVE',  'Approve Customer Invoices',   'Approve customer invoices and post journal entries'),
('CUSTOMER_INVOICE_CANCEL',   'Cancel Customer Invoices',    'Cancel approved customer invoices and reverse journal entries'),
('CUSTOMER_INVOICE_VOID',     'Void Customer Invoices',      'Void draft or submitted customer invoices')
ON CONFLICT (code) DO NOTHING;

-- Assign to ULTIMATE_ADMIN role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ULTIMATE_ADMIN'
  AND p.code IN (
    'CUSTOMER_INVOICE_CREATE', 'CUSTOMER_INVOICE_VIEW', 'CUSTOMER_INVOICE_UPDATE',
    'CUSTOMER_INVOICE_SUBMIT', 'CUSTOMER_INVOICE_APPROVE', 'CUSTOMER_INVOICE_CANCEL',
    'CUSTOMER_INVOICE_VOID'
  )
ON CONFLICT (role_id, permission_id) DO NOTHING;
