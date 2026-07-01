-- V124: Supplier Qualification and Risk Analysis
CREATE TABLE IF NOT EXISTS procurement_supplier_qualifications (
    id BIGSERIAL PRIMARY KEY,
    supplier_id BIGINT NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL DEFAULT 'ONBOARDING',
    risk_score_financial NUMERIC(5, 2) DEFAULT 0,
    risk_score_compliance NUMERIC(5, 2) DEFAULT 0,
    risk_score_esg NUMERIC(5, 2) DEFAULT 0,
    consolidated_risk_level VARCHAR(20) NOT NULL DEFAULT 'LOW',
    approved_vendor_list BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
