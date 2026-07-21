-- Flyway Migration: Add bank details to employees table
ALTER TABLE employees ADD COLUMN bank_name VARCHAR(100);
ALTER TABLE employees ADD COLUMN bank_account_number VARCHAR(100);
ALTER TABLE employees ADD COLUMN bank_routing_number VARCHAR(100);
