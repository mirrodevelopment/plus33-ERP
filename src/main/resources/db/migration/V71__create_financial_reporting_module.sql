-- ============================================================
-- V71__create_financial_reporting_module.sql
-- PLUS33 ERP — Financial Reporting & Locking Tables
-- ============================================================

-- 1. Alter companies to support configurable fiscal calendar (defaults to calendar year)
ALTER TABLE companies ADD COLUMN fiscal_year_start_month INTEGER NOT NULL DEFAULT 1;
ALTER TABLE companies ADD COLUMN fiscal_year_start_day INTEGER NOT NULL DEFAULT 1;
ALTER TABLE companies ADD CONSTRAINT chk_fiscal_month CHECK (fiscal_year_start_month BETWEEN 1 AND 12);
ALTER TABLE companies ADD CONSTRAINT chk_fiscal_day CHECK (fiscal_year_start_day BETWEEN 1 AND 31);

-- 2. Alter journal_entries to track closing entries and closing types
ALTER TABLE journal_entries ADD COLUMN closing_entry BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE journal_entries ADD COLUMN closing_type VARCHAR(30);
ALTER TABLE journal_entries ADD CONSTRAINT chk_closing_type CHECK (closing_type IS NULL OR closing_type IN ('NORMAL', 'ADJUSTMENT', 'REOPEN_REVERSAL'));

-- 3. Create fiscal_years table with opening and closing journal references
CREATE TABLE fiscal_years (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    fiscal_year INTEGER NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    opening_journal_id BIGINT,
    closing_journal_id BIGINT,
    closed_by VARCHAR(100),
    closed_at TIMESTAMP,

    CONSTRAINT fk_fiscal_years_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id),
        
    CONSTRAINT fk_fiscal_years_opening_je
        FOREIGN KEY (opening_journal_id)
        REFERENCES journal_entries(id),
        
    CONSTRAINT fk_fiscal_years_closing_je
        FOREIGN KEY (closing_journal_id)
        REFERENCES journal_entries(id),
        
    CONSTRAINT uk_fiscal_year_company
        UNIQUE (company_id, fiscal_year),
        
    CONSTRAINT chk_fy_status
        CHECK (status IN ('OPEN', 'CLOSED'))
);

-- 4. Create period_locks table with lock levels
CREATE TABLE period_locks (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL UNIQUE,
    lock_date DATE NOT NULL,
    lock_type VARCHAR(30) NOT NULL DEFAULT 'HARD',
    locked_by VARCHAR(100) NOT NULL,
    locked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reason VARCHAR(255),

    CONSTRAINT fk_period_locks_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id),
        
    CONSTRAINT chk_lock_type
        CHECK (lock_type IN ('SOFT', 'HARD'))
);

-- 5. Create period_lock_overrides audit table
CREATE TABLE period_lock_overrides (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    user_email VARCHAR(100) NOT NULL,
    override_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    original_lock_date DATE NOT NULL,
    transaction_date DATE NOT NULL,
    journal_entry_id BIGINT,
    reason VARCHAR(255),

    CONSTRAINT fk_lock_overrides_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id),
        
    CONSTRAINT fk_lock_overrides_je
        FOREIGN KEY (journal_entry_id)
        REFERENCES journal_entries(id)
);

-- 6. Create exchange_rates table supporting rate types and strict validations
CREATE TABLE exchange_rates (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    from_currency VARCHAR(3) NOT NULL,
    to_currency VARCHAR(3) NOT NULL,
    rate_type VARCHAR(30) NOT NULL DEFAULT 'SPOT',
    rate DECIMAL(12,6) NOT NULL,
    effective_date DATE NOT NULL,

    CONSTRAINT fk_exchange_rates_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id),
        
    CONSTRAINT uk_exchange_rate_lookup
        UNIQUE (company_id, from_currency, to_currency, rate_type, effective_date),
        
    CONSTRAINT chk_rate_positive
        CHECK (rate > 0),
        
    CONSTRAINT chk_rate_currencies_different
        CHECK (from_currency <> to_currency),
        
    CONSTRAINT chk_rate_type
        CHECK (rate_type IN ('SPOT', 'CORPORATE', 'MONTH_END', 'AVERAGE'))
);

-- 7. Seed Retained Earnings account (3100) under Equity (3000) for all companies
INSERT INTO chart_of_accounts (company_id, account_code, account_name, account_type, parent_account_id, active)
SELECT c.id, '3100', 'Retained Earnings', 'EQUITY', parent.id, TRUE
FROM companies c
JOIN chart_of_accounts parent ON parent.company_id = c.id AND parent.account_code = '3000';

-- 8. Create performance indexes
CREATE INDEX idx_je_company_status_date ON journal_entries(company_id, status, entry_date);
CREATE INDEX idx_je_company_currency ON journal_entries(company_id, currency_code);
CREATE INDEX idx_jel_account_entry ON journal_entry_lines(account_id);
CREATE INDEX idx_ex_rate_lookup ON exchange_rates(company_id, from_currency, to_currency, rate_type, effective_date);
