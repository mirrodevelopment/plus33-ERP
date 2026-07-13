-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 20
-- File              : V20__seed_chart_of_accounts.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed chart of accounts
--
-- Tables Created    : N/A
-- Tables Altered    : N/A
-- Seed Data For     : chart_of_accounts
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V20__seed_chart_of_accounts.sql
-- PLUS33 ERP — Standard Chart of Accounts Seed Data
-- ============================================================

-- Parent Account: Assets
INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, active)
SELECT id, '1000', 'Assets', 'ASSET', TRUE 
FROM companies 
WHERE code = 'PLUS33_GLOBAL';

-- Child Accounts under Assets
INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, parent_account_id, active)
SELECT c.id, '1100', 'Cash', 'ASSET', parent.id, TRUE 
FROM companies c
CROSS JOIN chart_of_accounts parent
WHERE c.code = 'PLUS33_GLOBAL' AND parent.account_code = '1000' AND parent.company_id = c.id;

INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, parent_account_id, active)
SELECT c.id, '1200', 'Bank', 'ASSET', parent.id, TRUE 
FROM companies c
CROSS JOIN chart_of_accounts parent
WHERE c.code = 'PLUS33_GLOBAL' AND parent.account_code = '1000' AND parent.company_id = c.id;

INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, parent_account_id, active)
SELECT c.id, '1300', 'Inventory', 'ASSET', parent.id, TRUE 
FROM companies c
CROSS JOIN chart_of_accounts parent
WHERE c.code = 'PLUS33_GLOBAL' AND parent.account_code = '1000' AND parent.company_id = c.id;

-- Parent Account: Liabilities
INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, active)
SELECT id, '2000', 'Liabilities', 'LIABILITY', TRUE 
FROM companies 
WHERE code = 'PLUS33_GLOBAL';

-- Child Accounts under Liabilities
INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, parent_account_id, active)
SELECT c.id, '2100', 'Accounts Payable', 'LIABILITY', parent.id, TRUE 
FROM companies c
CROSS JOIN chart_of_accounts parent
WHERE c.code = 'PLUS33_GLOBAL' AND parent.account_code = '2000' AND parent.company_id = c.id;

-- Parent Account: Equity
INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, active)
SELECT id, '3000', 'Equity', 'EQUITY', TRUE 
FROM companies 
WHERE code = 'PLUS33_GLOBAL';

-- Parent Account: Revenue
INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, active)
SELECT id, '4000', 'Revenue', 'REVENUE', TRUE 
FROM companies 
WHERE code = 'PLUS33_GLOBAL';

-- Parent Account: Expenses
INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, active)
SELECT id, '5000', 'Expenses', 'EXPENSE', TRUE 
FROM companies 
WHERE code = 'PLUS33_GLOBAL';

-- Child Accounts under Expenses
INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, parent_account_id, active)
SELECT c.id, '5100', 'Payroll Expense', 'EXPENSE', parent.id, TRUE 
FROM companies c
CROSS JOIN chart_of_accounts parent
WHERE c.code = 'PLUS33_GLOBAL' AND parent.account_code = '5000' AND parent.company_id = c.id;

INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, parent_account_id, active)
SELECT c.id, '5200', 'Cost of Goods Sold', 'EXPENSE', parent.id, TRUE 
FROM companies c
CROSS JOIN chart_of_accounts parent
WHERE c.code = 'PLUS33_GLOBAL' AND parent.account_code = '5000' AND parent.company_id = c.id;
