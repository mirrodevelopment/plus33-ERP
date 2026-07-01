-- V164: Compliance Evidence Vault
CREATE TABLE IF NOT EXISTS grc_compliance_evidence (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    reference_type VARCHAR(50) NOT NULL,
    reference_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    content_hash VARCHAR(64) NOT NULL UNIQUE,
    evidence_source VARCHAR(100),
    uploaded_by_id BIGINT NOT NULL,
    verified_by_id BIGINT,
    verification_date DATE,
    retention_policy VARCHAR(50) NOT NULL DEFAULT '7_YEARS',
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
