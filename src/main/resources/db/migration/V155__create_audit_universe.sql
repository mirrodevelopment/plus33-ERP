-- V155: Audit Universe & Programs
CREATE TABLE IF NOT EXISTS grc_audit_universe (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    entity_name VARCHAR(150) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    risk_score NUMERIC(5, 2) NOT NULL DEFAULT 0.00,
    last_audited DATE
);

CREATE TABLE IF NOT EXISTS grc_audit_programs (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    program_name VARCHAR(150) NOT NULL,
    fiscal_year INT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PLANNED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
