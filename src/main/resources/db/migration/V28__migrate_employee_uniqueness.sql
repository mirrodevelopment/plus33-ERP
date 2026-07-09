-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 28
-- File              : V28__migrate_employee_uniqueness.sql
-- Operation Type    : Schema Alteration
-- Purpose           : migrate employee uniqueness
--
-- Tables Created    : N/A
-- Tables Altered    : employees, employees, employees
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V28__migrate_employee_uniqueness.sql
-- PLUS33 ERP — Employee management uniqueness constraints migration
-- ============================================================

-- Drop global unique constraint on employee_code
ALTER TABLE employees DROP CONSTRAINT IF EXISTS employees_employee_code_key;

-- Add company-scoped composite unique constraints
ALTER TABLE employees ADD CONSTRAINT uk_employees_company_code UNIQUE (company_id, employee_code);
ALTER TABLE employees ADD CONSTRAINT uk_employees_company_email UNIQUE (company_id, email);
