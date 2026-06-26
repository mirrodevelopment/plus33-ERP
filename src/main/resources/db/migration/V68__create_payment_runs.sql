-- ============================================================
-- V68__create_payment_runs.sql
-- PLUS33 ERP — Supplier Payments & Payment Runs Schema
-- ============================================================

-- 1. Alter suppliers to add bank detail columns
ALTER TABLE suppliers ADD COLUMN bank_name VARCHAR(150);
ALTER TABLE suppliers ADD COLUMN bank_account_number VARCHAR(50);
ALTER TABLE suppliers ADD COLUMN swift_code VARCHAR(30);
ALTER TABLE suppliers ADD COLUMN iban VARCHAR(50);

-- 2. Create payment_runs table
CREATE SEQUENCE payment_run_seq START WITH 1;

CREATE TABLE payment_runs (
    id BIGSERIAL PRIMARY KEY,
    run_number VARCHAR(50) NOT NULL UNIQUE,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    status VARCHAR(30) NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    filter_due_date DATE,
    filter_supplier_id BIGINT REFERENCES suppliers(id),
    bank_account_code VARCHAR(20) NOT NULL DEFAULT '1200',
    total_amount DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    export_format VARCHAR(30) NOT NULL DEFAULT 'CSV',
    export_content TEXT,
    client_reference_id UUID UNIQUE,
    
    -- Execution audit metrics
    successful_payments_count INT NOT NULL DEFAULT 0,
    failed_payments_count INT NOT NULL DEFAULT 0,
    processed_invoices_count INT NOT NULL DEFAULT 0,
    failure_reason TEXT,
    
    -- Status audit trail
    approved_by BIGINT REFERENCES users(id),
    approved_at TIMESTAMP,
    executed_by BIGINT REFERENCES users(id),
    executed_at TIMESTAMP,
    cancelled_by BIGINT REFERENCES users(id),
    cancelled_at TIMESTAMP,
    
    -- Core audit fields
    created_by BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

-- 3. Create payment_run_invoices table
CREATE TABLE payment_run_invoices (
    id BIGSERIAL PRIMARY KEY,
    payment_run_id BIGINT NOT NULL REFERENCES payment_runs(id) ON DELETE CASCADE,
    supplier_invoice_id BIGINT NOT NULL REFERENCES supplier_invoices(id),
    invoice_outstanding_balance DECIMAL(14,2) NOT NULL,
    payment_amount DECIMAL(14,2) NOT NULL CHECK (payment_amount > 0.00),
    payment_reference VARCHAR(100) NOT NULL,
    CONSTRAINT uq_payment_run_invoice UNIQUE (payment_run_id, supplier_invoice_id)
);

-- 4. Alter supplier_invoices to add payment_run_id for invoice reservation
ALTER TABLE supplier_invoices ADD COLUMN payment_run_id BIGINT REFERENCES payment_runs(id);

-- 5. Add indexes for query performance and isolation boundaries
CREATE INDEX idx_payment_runs_company ON payment_runs(company_id);
CREATE INDEX idx_payment_runs_status ON payment_runs(status);
CREATE INDEX idx_payment_run_invoices_run ON payment_run_invoices(payment_run_id);
CREATE INDEX idx_payment_run_invoices_invoice ON payment_run_invoices(supplier_invoice_id);
CREATE INDEX idx_supplier_invoices_payment_run ON supplier_invoices(payment_run_id);
