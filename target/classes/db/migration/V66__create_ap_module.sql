-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 66
-- File              : V66__create_ap_module.sql
-- Operation Type    : Schema Alteration
-- Purpose           : create ap module
--
-- Tables Created    : N/A
-- Tables Altered    : supplier_invoices, supplier_invoices
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V66__create_ap_module.sql
-- PLUS33 ERP — Accounts Payable Module Schema Enhancements
-- ============================================================

-- 1. Drop the existing status check constraint on supplier_invoices if it exists
ALTER TABLE supplier_invoices DROP CONSTRAINT IF EXISTS chk_supplier_invoice_status;

-- 2. Recreate the check constraint to support SUBMITTED and VOID statuses
ALTER TABLE supplier_invoices ADD CONSTRAINT chk_supplier_invoice_status
    CHECK (status IN ('DRAFT', 'SUBMITTED', 'APPROVED', 'PARTIALLY_PAID', 'PAID', 'CANCELLED', 'VOID'));
