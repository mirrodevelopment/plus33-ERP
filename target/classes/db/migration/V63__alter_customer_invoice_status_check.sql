-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 63
-- File              : V63__alter_customer_invoice_status_check.sql
-- Operation Type    : Schema Alteration
-- Purpose           : alter customer invoice status check
--
-- Tables Created    : N/A
-- Tables Altered    : customer_invoices, customer_invoices
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V63__alter_customer_invoice_status_check.sql
-- PLUS33 ERP — Update Customer Invoice Status Check Constraint
-- ============================================================

ALTER TABLE customer_invoices DROP CONSTRAINT chk_customer_invoice_status;

ALTER TABLE customer_invoices ADD CONSTRAINT chk_customer_invoice_status CHECK (
    status IN ('DRAFT', 'SUBMITTED', 'APPROVED', 'PARTIALLY_PAID', 'PAID', 'PARTIALLY_CREDITED', 'FULLY_CREDITED', 'CANCELLED', 'VOID')
);
