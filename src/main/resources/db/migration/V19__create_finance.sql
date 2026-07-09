-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 19
-- File              : V19__create_finance.sql
-- Operation Type    : Schema Creation
-- Purpose           : create finance
--
-- Tables Created    : chart_of_accounts, journal_entries, journal_entry_lines, supplier_invoices, payments, payment_allocations
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : idx_coa_company, idx_coa_parent, idx_journal_entries_company, idx_journal_entries_created_by, idx_journal_entries_reversal, idx_journal_lines_entry, idx_journal_lines_account, idx_supplier_invoice_company, idx_supplier_invoice_supplier, idx_supplier_invoice_po, idx_payments_company, idx_payments_journal, idx_payments_user, idx_payment_allocations_payment, idx_payment_allocations_invoice
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V19__create_finance.sql
-- PLUS33 ERP — Finance & Accounting Tables
-- ============================================================

CREATE TABLE chart_of_accounts (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    account_code VARCHAR(50) NOT NULL,
    account_name VARCHAR(150) NOT NULL,
    account_type VARCHAR(30) NOT NULL,
    parent_account_id BIGINT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_coa_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id),

    CONSTRAINT fk_coa_parent
        FOREIGN KEY (parent_account_id)
        REFERENCES chart_of_accounts(id),

    CONSTRAINT uk_coa_company_code
        UNIQUE (company_id, account_code),

    CONSTRAINT chk_coa_type
        CHECK (account_type IN ('ASSET', 'LIABILITY', 'EQUITY', 'REVENUE', 'EXPENSE'))
);

CREATE TABLE journal_entries (
    id BIGSERIAL PRIMARY KEY,
    entry_number VARCHAR(50) NOT NULL,
    company_id BIGINT NOT NULL,
    entry_date DATE NOT NULL,
    description TEXT,
    source_module VARCHAR(50) NOT NULL,
    source_reference VARCHAR(100),
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    reversal_entry_id BIGINT,
    currency_code VARCHAR(3) NOT NULL DEFAULT 'AED',
    posted_at TIMESTAMP,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_journal_entries_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id),

    CONSTRAINT fk_journal_entries_user
        FOREIGN KEY (created_by)
        REFERENCES users(id),

    CONSTRAINT fk_journal_reversal
        FOREIGN KEY (reversal_entry_id)
        REFERENCES journal_entries(id),

    CONSTRAINT uk_journal_company_entry
        UNIQUE (company_id, entry_number),

    CONSTRAINT chk_journal_status
        CHECK (status IN ('DRAFT', 'POSTED', 'REVERSED')),

    CONSTRAINT chk_journal_reversal_self
        CHECK (reversal_entry_id IS NULL OR reversal_entry_id <> id)
);

CREATE TABLE journal_entry_lines (
    id BIGSERIAL PRIMARY KEY,
    journal_entry_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    debit_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
    credit_amount DECIMAL(14,2) NOT NULL DEFAULT 0,

    CONSTRAINT fk_journal_lines_entry
        FOREIGN KEY (journal_entry_id)
        REFERENCES journal_entries(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_journal_lines_account
        FOREIGN KEY (account_id)
        REFERENCES chart_of_accounts(id),

    CONSTRAINT chk_debit_credit
        CHECK (
            (debit_amount > 0 AND credit_amount = 0)
            OR
            (credit_amount > 0 AND debit_amount = 0)
        )
);

CREATE TABLE supplier_invoices (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    purchase_order_id BIGINT,
    invoice_number VARCHAR(100) NOT NULL,
    invoice_date DATE NOT NULL,
    due_date DATE,
    total_amount DECIMAL(14,2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    currency_code VARCHAR(3) NOT NULL DEFAULT 'AED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_supplier_invoice_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id),

    CONSTRAINT fk_supplier_invoice_supplier
        FOREIGN KEY (supplier_id)
        REFERENCES suppliers(id),

    CONSTRAINT fk_supplier_invoice_po
        FOREIGN KEY (purchase_order_id)
        REFERENCES purchase_orders(id),

    CONSTRAINT uk_supplier_invoice
        UNIQUE (supplier_id, invoice_number),

    CONSTRAINT chk_supplier_invoice_status
        CHECK (status IN ('PENDING', 'APPROVED', 'PAID', 'OVERDUE', 'CANCELLED')),

    CONSTRAINT chk_supplier_invoice_amount
        CHECK (total_amount > 0)
);

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    payment_number VARCHAR(50) NOT NULL,
    company_id BIGINT NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    payment_type VARCHAR(30) NOT NULL,
    amount DECIMAL(14,2) NOT NULL,
    reference_number VARCHAR(100),
    journal_entry_id BIGINT,
    currency_code VARCHAR(3) NOT NULL DEFAULT 'AED',
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payments_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id),

    CONSTRAINT fk_payments_journal
        FOREIGN KEY (journal_entry_id)
        REFERENCES journal_entries(id),

    CONSTRAINT fk_payments_user
        FOREIGN KEY (created_by)
        REFERENCES users(id),

    CONSTRAINT uk_payment_company_number
        UNIQUE (company_id, payment_number),

    CONSTRAINT chk_payment_method
        CHECK (payment_method IN ('CASH', 'BANK_TRANSFER', 'CARD', 'UPI', 'CHEQUE')),

    CONSTRAINT chk_payment_type
        CHECK (payment_type IN ('RECEIVABLE', 'PAYABLE')),

    CONSTRAINT chk_payment_amount
        CHECK (amount > 0)
);

CREATE TABLE payment_allocations (
    id BIGSERIAL PRIMARY KEY,
    payment_id BIGINT NOT NULL,
    supplier_invoice_id BIGINT NOT NULL,
    allocated_amount DECIMAL(14,2) NOT NULL,

    CONSTRAINT fk_payment_allocations_payment
        FOREIGN KEY (payment_id)
        REFERENCES payments(id),

    CONSTRAINT fk_payment_allocations_invoice
        FOREIGN KEY (supplier_invoice_id)
        REFERENCES supplier_invoices(id),

    CONSTRAINT chk_payment_allocation_amount
        CHECK (allocated_amount > 0)
);

-- Performance Indexes for Foreign Keys
CREATE INDEX idx_coa_company ON chart_of_accounts(company_id);
CREATE INDEX idx_coa_parent ON chart_of_accounts(parent_account_id);
CREATE INDEX idx_journal_entries_company ON journal_entries(company_id);
CREATE INDEX idx_journal_entries_created_by ON journal_entries(created_by);
CREATE INDEX idx_journal_entries_reversal ON journal_entries(reversal_entry_id);
CREATE INDEX idx_journal_lines_entry ON journal_entry_lines(journal_entry_id);
CREATE INDEX idx_journal_lines_account ON journal_entry_lines(account_id);
CREATE INDEX idx_supplier_invoice_company ON supplier_invoices(company_id);
CREATE INDEX idx_supplier_invoice_supplier ON supplier_invoices(supplier_id);
CREATE INDEX idx_supplier_invoice_po ON supplier_invoices(purchase_order_id);
CREATE INDEX idx_payments_company ON payments(company_id);
CREATE INDEX idx_payments_journal ON payments(journal_entry_id);
CREATE INDEX idx_payments_user ON payments(created_by);
CREATE INDEX idx_payment_allocations_payment ON payment_allocations(payment_id);
CREATE INDEX idx_payment_allocations_invoice ON payment_allocations(supplier_invoice_id);
