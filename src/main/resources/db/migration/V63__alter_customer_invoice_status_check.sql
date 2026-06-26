-- ============================================================
-- V63__alter_customer_invoice_status_check.sql
-- PLUS33 ERP — Update Customer Invoice Status Check Constraint
-- ============================================================

ALTER TABLE customer_invoices DROP CONSTRAINT chk_customer_invoice_status;

ALTER TABLE customer_invoices ADD CONSTRAINT chk_customer_invoice_status CHECK (
    status IN ('DRAFT', 'SUBMITTED', 'APPROVED', 'PARTIALLY_PAID', 'PAID', 'PARTIALLY_CREDITED', 'FULLY_CREDITED', 'CANCELLED', 'VOID')
);
