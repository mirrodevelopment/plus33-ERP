-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 24
-- File              : V24__migrate_suppliers_to_company_scope.sql
-- Operation Type    : Schema Alteration
-- Purpose           : migrate suppliers to company scope
--
-- Tables Created    : N/A
-- Tables Altered    : suppliers, suppliers, suppliers, suppliers, suppliers, suppliers
-- Seed Data For     : N/A
-- Indexes           : idx_suppliers_company
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V24__migrate_suppliers_to_company_scope.sql
-- PLUS33 ERP — Schema alteration for company scoping of suppliers
-- ============================================================

-- Add company_id column
ALTER TABLE suppliers ADD COLUMN company_id BIGINT;

-- Backfill company_id with dynamic lookup for 'PLUS33_GLOBAL'
UPDATE suppliers
SET company_id = (SELECT id FROM companies WHERE code = 'PLUS33_GLOBAL' LIMIT 1);

-- Set company_id to NOT NULL
ALTER TABLE suppliers ALTER COLUMN company_id SET NOT NULL;

-- Add foreign key constraint
ALTER TABLE suppliers ADD CONSTRAINT fk_suppliers_company
    FOREIGN KEY (company_id) REFERENCES companies(id);

-- Drop the global unique constraint on code and replace with company-scoped unique constraint
ALTER TABLE suppliers DROP CONSTRAINT IF EXISTS suppliers_code_key;
ALTER TABLE suppliers ADD CONSTRAINT uk_suppliers_company_code UNIQUE (company_id, code);

-- Add company-scoped unique constraint on email
ALTER TABLE suppliers ADD CONSTRAINT uk_suppliers_company_email UNIQUE (company_id, email);

-- Index for foreign key optimization
CREATE INDEX idx_suppliers_company ON suppliers(company_id);
