-- ============================================================
-- V28__migrate_employee_uniqueness.sql
-- PLUS33 ERP — Employee management uniqueness constraints migration
-- ============================================================

-- Drop global unique constraint on employee_code
ALTER TABLE employees DROP CONSTRAINT IF EXISTS employees_employee_code_key;

-- Add company-scoped composite unique constraints
ALTER TABLE employees ADD CONSTRAINT uk_employees_company_code UNIQUE (company_id, employee_code);
ALTER TABLE employees ADD CONSTRAINT uk_employees_company_email UNIQUE (company_id, email);
