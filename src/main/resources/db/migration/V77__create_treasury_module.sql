-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 77
-- File              : V77__create_treasury_module.sql
-- Operation Type    : Schema Creation
-- Purpose           : create treasury module
--
-- Tables Created    : banks, bank_branches, bank_accounts, bank_virtual_accounts, in_house_bank_accounts, intercompany_loans, internal_settlements, cash_position_snapshots, cash_position_snapshot_lines, treasury_limits, cash_pools, cash_pool_members, payment_batches, payment_files, payment_transmission_logs, cash_transfers, treasury_approval_steps, treasury_approvals, bank_statements, bank_statement_lines, reconciliation_rules, bank_fee_rules, treasury_investments, treasury_compliance_logs
-- Tables Altered    : payments, payments
-- Seed Data For     : N/A
-- Indexes           : uk_limits_combination
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V77__create_treasury_module.sql
-- 1. Corporate Banks & Branches (with Counterparty Risk)
CREATE TABLE banks (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    routing_code VARCHAR(30),
    credit_rating VARCHAR(10) NOT NULL DEFAULT 'A', -- AAA, AA+, A, BBB, etc.
    country_risk_score INTEGER NOT NULL DEFAULT 1, -- 1 (Low) to 5 (High)
    exposure_limit DECIMAL(15,2) NOT NULL DEFAULT 10000000.00,
    max_deposit_limit DECIMAL(15,2) NOT NULL DEFAULT 10000000.00,
    current_exposure DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE bank_branches (
    id BIGSERIAL PRIMARY KEY,
    bank_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    swift_code VARCHAR(30) NOT NULL,
    address VARCHAR(255),
    
    CONSTRAINT fk_branches_bank FOREIGN KEY (bank_id) REFERENCES banks(id),
    CONSTRAINT uk_branches_bank_code UNIQUE (bank_id, code)
);

-- 2. Bank Accounts, Virtual Accounts, and Intraday Balances
CREATE TABLE bank_accounts (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    account_name VARCHAR(150) NOT NULL,
    account_number VARCHAR(50) NOT NULL,
    iban VARCHAR(50),
    currency_code VARCHAR(3) NOT NULL DEFAULT 'AED',
    gl_account_id BIGINT NOT NULL,
    account_type VARCHAR(30) NOT NULL DEFAULT 'CURRENT', -- CURRENT, SAVINGS, OVERDRAFT, ESCROW
    opening_balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    current_balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    reconciled_balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    projected_closing_balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_bank_accounts_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_bank_accounts_branch FOREIGN KEY (branch_id) REFERENCES bank_branches(id),
    CONSTRAINT fk_bank_accounts_gl FOREIGN KEY (gl_account_id) REFERENCES chart_of_accounts(id),
    CONSTRAINT uk_bank_accounts_number UNIQUE (company_id, account_number)
);

CREATE TABLE bank_virtual_accounts (
    id BIGSERIAL PRIMARY KEY,
    parent_account_id BIGINT NOT NULL,
    virtual_account_number VARCHAR(50) NOT NULL UNIQUE,
    owner_reference_type VARCHAR(30) NOT NULL, -- CUSTOMER, SUPPLIER
    owner_reference_id BIGINT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_va_parent FOREIGN KEY (parent_account_id) REFERENCES bank_accounts(id)
);

-- 3. In-House Bank (IHB) & Subsidiaries
CREATE TABLE in_house_bank_accounts (
    id BIGSERIAL PRIMARY KEY,
    subsidiary_company_id BIGINT NOT NULL,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    currency_code VARCHAR(3) NOT NULL DEFAULT 'AED',
    current_balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    gl_account_id BIGINT NOT NULL, -- Internal Intercompany Settlement Account
    active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_ihb_company FOREIGN KEY (subsidiary_company_id) REFERENCES companies(id),
    CONSTRAINT fk_ihb_gl FOREIGN KEY (gl_account_id) REFERENCES chart_of_accounts(id)
);

CREATE TABLE intercompany_loans (
    id BIGSERIAL PRIMARY KEY,
    lender_company_id BIGINT NOT NULL,
    borrower_company_id BIGINT NOT NULL,
    principal_amount DECIMAL(15,2) NOT NULL,
    interest_rate DECIMAL(5,2) NOT NULL, -- Intercompany interest rate
    start_date DATE NOT NULL,
    maturity_date DATE,
    interest_accrued DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, SETTLED, DEFAULTED
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_loan_lender FOREIGN KEY (lender_company_id) REFERENCES companies(id),
    CONSTRAINT fk_loan_borrower FOREIGN KEY (borrower_company_id) REFERENCES companies(id)
);

CREATE TABLE internal_settlements (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    source_ihb_account_id BIGINT NOT NULL,
    target_ihb_account_id BIGINT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    settlement_date DATE NOT NULL,
    reference_number VARCHAR(100) NOT NULL,
    notes VARCHAR(255),

    CONSTRAINT fk_settle_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_settle_source FOREIGN KEY (source_ihb_account_id) REFERENCES in_house_bank_accounts(id),
    CONSTRAINT fk_settle_target FOREIGN KEY (target_ihb_account_id) REFERENCES in_house_bank_accounts(id)
);

-- 4. Cash Position Snapshots
CREATE TABLE cash_position_snapshots (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    snapshot_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    snapshot_type VARCHAR(30) NOT NULL DEFAULT 'END_OF_DAY', -- INTRADAY, END_OF_DAY
    total_cash_usd DECIMAL(15,2) NOT NULL,
    created_by VARCHAR(100) NOT NULL,

    CONSTRAINT fk_snapshots_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE TABLE cash_position_snapshot_lines (
    id BIGSERIAL PRIMARY KEY,
    snapshot_id BIGINT NOT NULL,
    bank_account_id BIGINT NOT NULL,
    current_balance DECIMAL(15,2) NOT NULL,
    reconciled_balance DECIMAL(15,2) NOT NULL,
    currency_code VARCHAR(3) NOT NULL,

    CONSTRAINT fk_snap_lines_snapshot FOREIGN KEY (snapshot_id) REFERENCES cash_position_snapshots(id) ON DELETE CASCADE,
    CONSTRAINT fk_snap_lines_account FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id)
);

-- 5. Treasury Policy Limits
CREATE TABLE treasury_limits (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    limit_type VARCHAR(50) NOT NULL, -- DAILY_TRANSFER, CURRENCY_EXPOSURE, COUNTRY_EXPOSURE, MINIMUM_CASH_RESERVE
    currency_code VARCHAR(3) DEFAULT 'AED',
    country_code VARCHAR(3),
    target_bank_id BIGINT,
    limit_amount DECIMAL(15,2) NOT NULL,
    warning_threshold_percent DECIMAL(5,2) DEFAULT 80.00,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_limits_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_limits_bank FOREIGN KEY (target_bank_id) REFERENCES banks(id)
);

CREATE UNIQUE INDEX uk_limits_combination ON treasury_limits (company_id, limit_type, (COALESCE(currency_code, '')), (COALESCE(country_code, '')), (COALESCE(target_bank_id, 0)));

-- 6. Cash Pools (Notional & Physical)
CREATE TABLE cash_pools (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    pool_name VARCHAR(150) NOT NULL,
    pool_type VARCHAR(30) NOT NULL DEFAULT 'ZERO_BALANCE', -- ZERO_BALANCE, NOTIONAL, TARGET_BALANCE
    header_account_id BIGINT NOT NULL,
    target_balance DECIMAL(15,2) DEFAULT 0.00,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_pools_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_pools_header FOREIGN KEY (header_account_id) REFERENCES bank_accounts(id)
);

CREATE TABLE cash_pool_members (
    id BIGSERIAL PRIMARY KEY,
    pool_id BIGINT NOT NULL,
    bank_account_id BIGINT NOT NULL UNIQUE,
    sweep_priority INTEGER NOT NULL DEFAULT 1,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_pool_members_pool FOREIGN KEY (pool_id) REFERENCES cash_pools(id) ON DELETE CASCADE,
    CONSTRAINT fk_pool_members_account FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id)
);

-- 7. Payment Factory Schema
CREATE TABLE payment_batches (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    batch_number VARCHAR(50) NOT NULL,
    source_bank_account_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT', -- DRAFT, PENDING_APPROVAL, APPROVED, TRANSMITTED, COMPLETED, REJECTED
    total_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    currency_code VARCHAR(3) NOT NULL DEFAULT 'AED',
    created_by VARCHAR(100) NOT NULL,
    approved_by VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_batches_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_batches_bank FOREIGN KEY (source_bank_account_id) REFERENCES bank_accounts(id),
    CONSTRAINT uk_batches_number UNIQUE (company_id, batch_number)
);

ALTER TABLE payments ADD COLUMN payment_batch_id BIGINT;
ALTER TABLE payments ADD CONSTRAINT fk_payments_batch FOREIGN KEY (payment_batch_id) REFERENCES payment_batches(id);

CREATE TABLE payment_files (
    id BIGSERIAL PRIMARY KEY,
    batch_id BIGINT NOT NULL UNIQUE,
    file_name VARCHAR(150) NOT NULL,
    file_format VARCHAR(30) NOT NULL, -- ISO20022_XML, SEPA_XML, NACHA_TXT, SWIFT_MT101
    file_content TEXT NOT NULL,
    generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_files_batch FOREIGN KEY (batch_id) REFERENCES payment_batches(id) ON DELETE CASCADE
);

CREATE TABLE payment_transmission_logs (
    id BIGSERIAL PRIMARY KEY,
    file_id BIGINT NOT NULL,
    transmission_method VARCHAR(30) NOT NULL, -- API, SFTP
    status VARCHAR(30) NOT NULL,
    request_payload TEXT,
    response_payload TEXT,
    error_message TEXT,
    transmitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transmission_file FOREIGN KEY (file_id) REFERENCES payment_files(id) ON DELETE CASCADE
);

-- 8. Treasury Inter-Account Transfers & Approval Steps
CREATE TABLE cash_transfers (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    source_bank_account_id BIGINT NOT NULL,
    destination_bank_account_id BIGINT NOT NULL,
    transfer_date DATE NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    exchange_rate DECIMAL(18,6) NOT NULL DEFAULT 1.000000,
    fees DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    reference_number VARCHAR(100) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT', -- DRAFT, PENDING_APPROVAL, APPROVED, COMPLETED, CANCELLED
    created_by VARCHAR(100) NOT NULL,
    approved_by VARCHAR(100),
    approved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transfers_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_transfers_source FOREIGN KEY (source_bank_account_id) REFERENCES bank_accounts(id),
    CONSTRAINT fk_transfers_dest FOREIGN KEY (destination_bank_account_id) REFERENCES bank_accounts(id)
);

CREATE TABLE treasury_approval_steps (
    id BIGSERIAL PRIMARY KEY,
    step_sequence INTEGER NOT NULL,
    role_code VARCHAR(50) NOT NULL,
    min_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    max_amount DECIMAL(15,2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE treasury_approvals (
    id BIGSERIAL PRIMARY KEY,
    approval_step INTEGER NOT NULL,
    role_code VARCHAR(50) NOT NULL,
    transfer_id BIGINT,
    payment_batch_id BIGINT,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    approver_username VARCHAR(100),
    approved_at TIMESTAMP,
    remarks VARCHAR(255),

    CONSTRAINT fk_approvals_transfer FOREIGN KEY (transfer_id) REFERENCES cash_transfers(id) ON DELETE CASCADE,
    CONSTRAINT fk_approvals_batch FOREIGN KEY (payment_batch_id) REFERENCES payment_batches(id) ON DELETE CASCADE
);

-- 9. Electronic Bank Statements
CREATE TABLE bank_statements (
    id BIGSERIAL PRIMARY KEY,
    bank_account_id BIGINT NOT NULL,
    statement_number VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    opening_balance DECIMAL(15,2) NOT NULL,
    closing_balance DECIMAL(15,2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT', -- DRAFT, RECONCILING, RECONCILED
    imported_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_statements_account FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id),
    CONSTRAINT uk_statements_num UNIQUE (bank_account_id, statement_number)
);

CREATE TABLE bank_statement_lines (
    id BIGSERIAL PRIMARY KEY,
    statement_id BIGINT NOT NULL,
    transaction_date DATE NOT NULL,
    value_date DATE,
    description VARCHAR(255) NOT NULL,
    reference VARCHAR(100),
    amount DECIMAL(15,2) NOT NULL,
    reconciled BOOLEAN NOT NULL DEFAULT FALSE,
    payment_id BIGINT,
    reconciled_at TIMESTAMP,

    CONSTRAINT fk_statement_lines_stmt FOREIGN KEY (statement_id) REFERENCES bank_statements(id) ON DELETE CASCADE,
    CONSTRAINT fk_statement_lines_payment FOREIGN KEY (payment_id) REFERENCES payments(id)
);

-- 10. Rules Engine & Matching Settings
CREATE TABLE reconciliation_rules (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    rule_name VARCHAR(150) NOT NULL,
    date_tolerance_days INTEGER DEFAULT 3,
    reference_match_type VARCHAR(30) NOT NULL DEFAULT 'EXACT',
    allow_many_to_one BOOLEAN NOT NULL DEFAULT FALSE,
    allow_one_to_many BOOLEAN NOT NULL DEFAULT FALSE,
    allow_splits BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_rules_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE TABLE bank_fee_rules (
    id BIGSERIAL PRIMARY KEY,
    bank_account_id BIGINT NOT NULL,
    charge_type VARCHAR(30) NOT NULL,
    rate_percent DECIMAL(5,2) DEFAULT 0.00,
    fixed_amount DECIMAL(15,2) DEFAULT 0.00,
    gl_expense_account_id BIGINT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_fee_rules_account FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id),
    CONSTRAINT fk_fee_rules_gl FOREIGN KEY (gl_expense_account_id) REFERENCES chart_of_accounts(id)
);

-- 11. Investments (with Yield Forecast details)
CREATE TABLE treasury_investments (
    id BIGSERIAL PRIMARY KEY,
    bank_account_id BIGINT NOT NULL,
    reference_number VARCHAR(100) NOT NULL UNIQUE,
    investment_type VARCHAR(50) NOT NULL,
    principal_amount DECIMAL(15,2) NOT NULL,
    interest_rate DECIMAL(5,2) NOT NULL,
    expected_yield DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    effective_yield DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    compounded_interest DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    tax_withholding DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    early_liquidation_penalty DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    start_date DATE NOT NULL,
    maturity_date DATE NOT NULL,
    interest_earned DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    accrued_interest DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_investments_account FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id)
);

CREATE TABLE treasury_compliance_logs (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    details TEXT NOT NULL,
    performed_by VARCHAR(100) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_compliance_company FOREIGN KEY (company_id) REFERENCES companies(id)
);
