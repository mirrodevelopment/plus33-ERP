-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 70
-- File              : V70__payment_run_file_and_supplier_results.sql
-- Operation Type    : Schema Creation
-- Purpose           : payment run file and supplier results
--
-- Tables Created    : payment_run_supplier_results
-- Tables Altered    : payment_runs, payment_runs, payment_runs, payment_runs, payment_runs
-- Seed Data For     : N/A
-- Indexes           : idx_payment_run_supplier_results
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V70__payment_run_file_and_supplier_results.sql
-- PLUS33 ERP — Supplier Results & Bank File Storage Metadata
-- ============================================================

-- 1. Alter payment_runs to replace export_content with file metadata
ALTER TABLE payment_runs DROP COLUMN export_content;
ALTER TABLE payment_runs ADD COLUMN export_file_name VARCHAR(255);
ALTER TABLE payment_runs ADD COLUMN export_storage_path VARCHAR(500);
ALTER TABLE payment_runs ADD COLUMN export_checksum VARCHAR(64);
ALTER TABLE payment_runs ADD COLUMN export_generated_at TIMESTAMP;

-- 2. Create payment_run_supplier_results table
CREATE TABLE payment_run_supplier_results (
    id BIGSERIAL PRIMARY KEY,
    payment_run_id BIGINT NOT NULL REFERENCES payment_runs(id) ON DELETE CASCADE,
    supplier_id BIGINT NOT NULL REFERENCES suppliers(id),
    payment_id BIGINT REFERENCES payments(id),
    status VARCHAR(30) NOT NULL,
    amount DECIMAL(14,2) NOT NULL,
    error_message TEXT,
    started_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP NOT NULL
);

-- 3. Add index for supplier results query performance
CREATE INDEX idx_payment_run_supplier_results ON payment_run_supplier_results(payment_run_id);
