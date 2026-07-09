-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 80
-- File              : V80__create_tax_module.sql
-- Operation Type    : Schema Creation
-- Purpose           : create tax module
--
-- Tables Created    : tax_categories, tax_posting_profiles, tax_rates, tax_groups, tax_group_lines, tax_registrations, tax_exemption_certificates, tax_determination_rules, tax_adjustment_entries, tax_override_requests, einvoice_compliance_logs, tax_audit_logs, tax_calendar, tax_filings
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : idx_tr_lookup, idx_mv_input_tax, idx_mv_output_tax, idx_mv_reverse_charge, idx_mv_wht, idx_mv_tax_filings, idx_mv_tax_dashboard
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V80__create_tax_module.sql
-- 1. Tax Categories
CREATE TABLE tax_categories (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- 2. Tax Posting Profiles
CREATE TABLE tax_posting_profiles (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    category_id BIGINT NOT NULL REFERENCES tax_categories(id),
    input_tax_account_id BIGINT REFERENCES chart_of_accounts(id),
    output_tax_account_id BIGINT REFERENCES chart_of_accounts(id),
    reverse_charge_account_id BIGINT REFERENCES chart_of_accounts(id),
    recoverable_account_id BIGINT REFERENCES chart_of_accounts(id),
    non_recoverable_account_id BIGINT REFERENCES chart_of_accounts(id),
    suspense_account_id BIGINT REFERENCES chart_of_accounts(id),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_tpp_company_category UNIQUE (company_id, category_id)
);

-- 3. Tax Rates (Versioned by date)
CREATE TABLE tax_rates (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL REFERENCES tax_categories(id),
    rate_percent DECIMAL(5,2) NOT NULL CHECK (rate_percent >= 0),
    effective_from DATE NOT NULL,
    effective_to DATE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_tr_dates CHECK (effective_to IS NULL OR effective_to >= effective_from)
);

-- 4. Tax Groups
CREATE TABLE tax_groups (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE tax_group_lines (
    id BIGSERIAL PRIMARY KEY,
    group_id BIGINT NOT NULL REFERENCES tax_groups(id),
    rate_id BIGINT NOT NULL REFERENCES tax_rates(id)
);

-- 5. Tax Registrations (Hierarchical)
CREATE TABLE tax_registrations (
    id BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(20) NOT NULL CHECK (entity_type IN ('COMPANY', 'CUSTOMER', 'SUPPLIER')),
    entity_id BIGINT NOT NULL,
    tax_scheme VARCHAR(50) NOT NULL, -- TRN, VAT, GST, TIN, PAN, ABN, EIN
    registration_number VARCHAR(100) NOT NULL,
    tax_office VARCHAR(150),
    filing_frequency VARCHAR(30) NOT NULL DEFAULT 'MONTHLY', -- MONTHLY, QUARTERLY, ANNUALLY
    filing_currency VARCHAR(3) NOT NULL DEFAULT 'AED',
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, SUSPENDED, CANCELLED
    effective_from DATE NOT NULL,
    effective_to DATE,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_tr_lookup ON tax_registrations(entity_type, entity_id, status);

-- 6. Tax Exemption Certificates
CREATE TABLE tax_exemption_certificates (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    customer_id BIGINT NOT NULL,
    certificate_number VARCHAR(100) NOT NULL UNIQUE,
    exemption_reason VARCHAR(100) NOT NULL,
    effective_from DATE NOT NULL,
    effective_to DATE NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_tec_dates CHECK (effective_to >= effective_from)
);

-- 7. Multidimensional Tax Determination Rules (With Priorities)
CREATE TABLE tax_determination_rules (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    rule_name VARCHAR(100) NOT NULL,
    priority INTEGER NOT NULL DEFAULT 100, -- Evaluated customer -> supplier -> product -> place of supply -> fallback
    customer_tax_profile VARCHAR(50),
    supplier_tax_profile VARCHAR(50),
    product_tax_category VARCHAR(50),
    origin_country VARCHAR(3),
    origin_state VARCHAR(50),
    dest_country VARCHAR(3),
    dest_state VARCHAR(50),
    incoterms VARCHAR(3),
    document_type VARCHAR(50) NOT NULL, -- Maps to document taxonomy
    tax_group_id BIGINT NOT NULL REFERENCES tax_groups(id),
    effective_from DATE NOT NULL,
    effective_to DATE,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- 8. Standalone Tax Adjustment Journals
CREATE TABLE tax_adjustment_entries (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    adjustment_date DATE NOT NULL,
    tax_category_id BIGINT NOT NULL REFERENCES tax_categories(id),
    gl_account_id BIGINT NOT NULL REFERENCES chart_of_accounts(id),
    debit_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    credit_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    description VARCHAR(255) NOT NULL,
    reason_code VARCHAR(50) NOT NULL, -- AUDIT_CORRECTION, MANUAL_ADJUSTMENT, GOVERNMENT_REASSESSMENT
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT', -- DRAFT, APPROVED, POSTED
    approved_by VARCHAR(100),
    approved_at TIMESTAMP
);

-- 9. Tax Override Requests (With workflow approval)
CREATE TABLE tax_override_requests (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    document_type VARCHAR(50) NOT NULL,
    document_id BIGINT NOT NULL,
    original_tax_amount DECIMAL(20,2) NOT NULL,
    requested_tax_amount DECIMAL(20,2) NOT NULL,
    reason TEXT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED
    reviewed_by VARCHAR(100),
    reviewed_at TIMESTAMP
);

-- 10. Compliance E-Invoice Logs
CREATE TABLE einvoice_compliance_logs (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    document_type VARCHAR(50) NOT NULL,
    document_id BIGINT NOT NULL,
    provider_type VARCHAR(50) NOT NULL,
    xml_payload TEXT NOT NULL,
    signature_hash VARCHAR(256) NOT NULL,
    status VARCHAR(30) NOT NULL,
    error_details TEXT,
    government_uuid VARCHAR(100),
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 11. Immutable Audit Trail Logs
CREATE TABLE tax_audit_logs (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    table_name VARCHAR(50) NOT NULL,
    row_id BIGINT NOT NULL,
    action VARCHAR(20) NOT NULL, -- INSERT, UPDATE, DELETE
    previous_state TEXT,
    new_state TEXT,
    actor VARCHAR(100) NOT NULL,
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 12. Tax Calendar & Filings
CREATE TABLE tax_calendar (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    filing_type VARCHAR(50) NOT NULL, -- VAT_RETURN, GST_RETURN, WHT_RETURN
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    due_date DATE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT' -- DRAFT, CALCULATED, REVIEWED, APPROVED, SUBMITTED, ACCEPTED, REJECTED, AMENDED
);

CREATE TABLE tax_filings (
    id BIGSERIAL PRIMARY KEY,
    calendar_id BIGINT NOT NULL REFERENCES tax_calendar(id),
    total_sales_amount DECIMAL(20,2) NOT NULL,
    total_input_tax DECIMAL(20,2) NOT NULL,
    total_output_tax DECIMAL(20,2) NOT NULL,
    net_tax_liability DECIMAL(20,2) NOT NULL,
    submission_payload TEXT,
    government_receipt_ref VARCHAR(100),
    filed_by VARCHAR(100) NOT NULL,
    filed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 13. Materialized Analytics Views

-- Input Tax View
CREATE OR REPLACE VIEW mv_input_tax_raw AS
SELECT 
    si.company_id,
    si.id AS invoice_id,
    si.invoice_number,
    si.invoice_date,
    s.name AS supplier_name,
    tr.registration_number AS supplier_trn,
    sii.net_amount,
    sii.tax_rate AS applied_rate,
    sii.tax_amount
FROM supplier_invoices si
JOIN supplier_invoice_items sii ON sii.supplier_invoice_id = si.id
JOIN suppliers s ON si.supplier_id = s.id
LEFT JOIN tax_registrations tr ON tr.entity_type = 'SUPPLIER' AND tr.entity_id = s.id AND tr.active = true
WHERE si.status = 'APPROVED';

DROP MATERIALIZED VIEW IF EXISTS mv_input_tax;
CREATE MATERIALIZED VIEW mv_input_tax AS SELECT * FROM mv_input_tax_raw;
CREATE UNIQUE INDEX idx_mv_input_tax ON mv_input_tax(invoice_id, applied_rate);

-- Output Tax View
CREATE OR REPLACE VIEW mv_output_tax_raw AS
SELECT 
    ci.company_id,
    ci.id AS invoice_id,
    ci.invoice_number,
    ci.invoice_date,
    c.name AS customer_name,
    tr.registration_number AS customer_trn,
    cii.net_amount,
    cii.tax_percentage AS applied_rate,
    cii.tax_amount
FROM customer_invoices ci
JOIN customer_invoice_items cii ON cii.customer_invoice_id = ci.id
JOIN customers c ON ci.customer_id = c.id
LEFT JOIN tax_registrations tr ON tr.entity_type = 'CUSTOMER' AND tr.entity_id = c.id AND tr.active = true
WHERE ci.status = 'APPROVED';

DROP MATERIALIZED VIEW IF EXISTS mv_output_tax;
CREATE MATERIALIZED VIEW mv_output_tax AS SELECT * FROM mv_output_tax_raw;
CREATE UNIQUE INDEX idx_mv_output_tax ON mv_output_tax(invoice_id, applied_rate);

-- Reverse Charge View
CREATE OR REPLACE VIEW mv_reverse_charge_raw AS
SELECT 
    si.company_id,
    si.id AS invoice_id,
    si.invoice_number,
    si.invoice_date,
    sii.net_amount,
    sii.tax_amount AS reverse_charge_tax
FROM supplier_invoices si
JOIN supplier_invoice_items sii ON sii.supplier_invoice_id = si.id
WHERE si.status = 'APPROVED' AND sii.tax_rate > 0;

DROP MATERIALIZED VIEW IF EXISTS mv_reverse_charge;
CREATE MATERIALIZED VIEW mv_reverse_charge AS SELECT * FROM mv_reverse_charge_raw;
CREATE UNIQUE INDEX idx_mv_reverse_charge ON mv_reverse_charge(invoice_id);

-- WHT Return View
CREATE OR REPLACE VIEW mv_withholding_tax_raw AS
SELECT 
    ba.company_id,
    ti.id AS investment_id,
    ti.principal_amount,
    ti.tax_withholding AS wht_amount,
    ti.status
FROM treasury_investments ti
JOIN bank_accounts ba ON ti.bank_account_id = ba.id
WHERE ti.tax_withholding > 0;

DROP MATERIALIZED VIEW IF EXISTS mv_withholding_tax;
CREATE MATERIALIZED VIEW mv_withholding_tax AS SELECT * FROM mv_withholding_tax_raw;
CREATE UNIQUE INDEX idx_mv_wht ON mv_withholding_tax(investment_id);

-- Tax Filings Register
CREATE OR REPLACE VIEW mv_tax_filings_raw AS
SELECT 
    tc.company_id,
    tc.id AS calendar_id,
    tc.filing_type,
    tc.period_start,
    tc.period_end,
    tc.status,
    tf.net_tax_liability,
    tf.filed_at
FROM tax_calendar tc
JOIN tax_filings tf ON tf.calendar_id = tc.id;

DROP MATERIALIZED VIEW IF EXISTS mv_tax_filings;
CREATE MATERIALIZED VIEW mv_tax_filings AS SELECT * FROM mv_tax_filings_raw;
CREATE UNIQUE INDEX idx_mv_tax_filings ON mv_tax_filings(calendar_id);

-- Tax Dashboard Summary
CREATE OR REPLACE VIEW mv_tax_dashboard_raw AS
SELECT 
    company_id,
    COALESCE(SUM(tax_amount), 0.00) AS total_output_tax,
    (SELECT COALESCE(SUM(tax_amount), 0.00) FROM mv_input_tax WHERE company_id = o.company_id) AS total_input_tax,
    COALESCE(SUM(tax_amount), 0.00) - (SELECT COALESCE(SUM(tax_amount), 0.00) FROM mv_input_tax WHERE company_id = o.company_id) AS net_liability
FROM mv_output_tax o
GROUP BY company_id;

DROP MATERIALIZED VIEW IF EXISTS mv_tax_dashboard;
CREATE MATERIALIZED VIEW mv_tax_dashboard AS SELECT * FROM mv_tax_dashboard_raw;
CREATE UNIQUE INDEX idx_mv_tax_dashboard ON mv_tax_dashboard(company_id);
