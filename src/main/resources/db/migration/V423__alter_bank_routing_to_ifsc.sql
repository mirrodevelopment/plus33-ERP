-- Flyway Migration: Rename bank_routing_number to ifsc_number
ALTER TABLE employees RENAME COLUMN bank_routing_number TO ifsc_number;
