-- V82__add_tax_indexes_and_refinements.sql
-- PLUS33 ERP — Tax refinements: indexes, versioning, calculation logs, compliance queue, reporting views, permissions

-- 1. Performance Indexes
CREATE INDEX idx_tax_rates_lookup ON tax_rates(category_id, effective_from, effective_to);
CREATE INDEX idx_tax_det_rules_lookup ON tax_determination_rules(company_id, effective_from, effective_to);
CREATE INDEX idx_tax_posting_profiles_lookup ON tax_posting_profiles(company_id, category_id);
CREATE INDEX idx_tax_registrations_effective ON tax_registrations(effective_from, effective_to);
CREATE INDEX idx_tax_exemption_certs_lookup ON tax_exemption_certificates(company_id, customer_id, effective_from, effective_to);

-- 1b. Tax Group engine type discriminator
ALTER TABLE tax_groups ADD COLUMN tax_type VARCHAR(30) NOT NULL DEFAULT 'VAT';

-- 2. Configuration Versioning
CREATE TABLE tax_configuration_versions (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    version_number INTEGER NOT NULL,
    effective_from TIMESTAMP NOT NULL,
    effective_to TIMESTAMP,
    published_by VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_company_version UNIQUE (company_id, version_number)
);

ALTER TABLE tax_rates ADD COLUMN config_version_id BIGINT REFERENCES tax_configuration_versions(id);
ALTER TABLE tax_determination_rules ADD COLUMN config_version_id BIGINT REFERENCES tax_configuration_versions(id);

-- 3. Calculation Audit Trail
CREATE TABLE tax_calculation_logs (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    document_type VARCHAR(50) NOT NULL,
    document_id BIGINT,
    request_payload TEXT NOT NULL, -- JSON formatted request
    resolved_rule_id BIGINT,
    applied_rate_percent DECIMAL(5,2),
    provider_name VARCHAR(100),
    calculated_tax_amount DECIMAL(15,2) NOT NULL,
    override_applied BOOLEAN NOT NULL DEFAULT FALSE,
    execution_duration_ms BIGINT NOT NULL,
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 4. Compliance Queue
CREATE TABLE compliance_queue_items (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    document_type VARCHAR(50) NOT NULL,
    document_id BIGINT NOT NULL,
    provider_type VARCHAR(50) NOT NULL,
    payload TEXT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING', -- PENDING, PROCESSING, COMPLETED, FAILED, DEAD_LETTER
    retry_count INTEGER NOT NULL DEFAULT 0,
    max_retries INTEGER NOT NULL DEFAULT 3,
    last_error TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 5. Additional reporting materialized views
-- a. Monthly Tax Trend View
CREATE OR REPLACE VIEW mv_monthly_tax_trend_raw AS
SELECT 
    company_id,
    TO_CHAR(invoice_date, 'YYYY-MM') AS filing_month,
    SUM(net_amount) AS total_net_sales,
    SUM(tax_amount) AS total_output_tax
FROM mv_output_tax
GROUP BY company_id, TO_CHAR(invoice_date, 'YYYY-MM');

DROP MATERIALIZED VIEW IF EXISTS mv_monthly_tax_trend;
CREATE MATERIALIZED VIEW mv_monthly_tax_trend AS SELECT * FROM mv_monthly_tax_trend_raw;

-- b. Tax by Jurisdiction View
CREATE OR REPLACE VIEW mv_tax_by_jurisdiction_raw AS
SELECT 
    o.company_id,
    c.country_code AS jurisdiction,
    SUM(o.tax_amount) AS total_tax_collected
FROM mv_output_tax o
JOIN companies c ON o.company_id = c.id
GROUP BY o.company_id, c.country_code;

DROP MATERIALIZED VIEW IF EXISTS mv_tax_by_jurisdiction;
CREATE MATERIALIZED VIEW mv_tax_by_jurisdiction AS SELECT * FROM mv_tax_by_jurisdiction_raw;

-- c. Tax by Product Category
CREATE OR REPLACE VIEW mv_tax_by_product_category_raw AS
SELECT 
    ci.company_id,
    p.category_id,
    SUM(cii.tax_amount) AS total_tax_amount
FROM customer_invoices ci
JOIN customer_invoice_items cii ON cii.customer_invoice_id = ci.id
JOIN products p ON cii.product_id = p.id
GROUP BY ci.company_id, p.category_id;

DROP MATERIALIZED VIEW IF EXISTS mv_tax_by_product_category;
CREATE MATERIALIZED VIEW mv_tax_by_product_category AS SELECT * FROM mv_tax_by_product_category_raw;

-- d. Tax Recovery Analysis
CREATE OR REPLACE VIEW mv_tax_recovery_analysis_raw AS
SELECT 
    company_id,
    SUM(tax_amount) AS total_input_tax,
    SUM(CASE WHEN applied_rate > 0 THEN tax_amount ELSE 0.00 END) AS recoverable_tax,
    SUM(CASE WHEN applied_rate = 0 THEN tax_amount ELSE 0.00 END) AS non_recoverable_tax
FROM mv_input_tax
GROUP BY company_id;

DROP MATERIALIZED VIEW IF EXISTS mv_tax_recovery_analysis;
CREATE MATERIALIZED VIEW mv_tax_recovery_analysis AS SELECT * FROM mv_tax_recovery_analysis_raw;

-- 6. Permissions and Role Maps
INSERT INTO permissions (code, name, description) VALUES
('TAX_VIEW', 'View Tax Configurations', 'View categories, rates and rules'),
('TAX_MANAGE', 'Manage Tax Configurations', 'Configure all tax properties'),
('TAX_OVERRIDE', 'Trigger Tax Overrides', 'Initiate tax override requests'),
('TAX_REPORT_EXPORT', 'Export Tax Reports', 'Download tax audit registers'),
('TAX_EINVOICE', 'Submit E-Invoices', 'Force manual submission of compliance payloads'),
('TAX_FILING', 'Process Tax Filings', 'Submit returns to government portal')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r CROSS JOIN permissions p
WHERE r.code IN ('ULTIMATE_ADMIN', 'TAX_MANAGER') AND p.code IN ('TAX_VIEW', 'TAX_MANAGE', 'TAX_OVERRIDE', 'TAX_REPORT_EXPORT', 'TAX_EINVOICE', 'TAX_FILING')
ON CONFLICT (role_id, permission_id) DO NOTHING;
