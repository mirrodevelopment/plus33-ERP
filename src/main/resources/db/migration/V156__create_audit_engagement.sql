-- V156: Audit Engagements & Findings
CREATE TABLE IF NOT EXISTS grc_audit_engagements (
    id BIGSERIAL PRIMARY KEY,
    program_id BIGINT NOT NULL,
    audit_universe_id BIGINT NOT NULL,
    engagement_number VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(200) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PLANNED',
    lead_auditor_id BIGINT,
    start_date DATE,
    end_date DATE
);

CREATE TABLE IF NOT EXISTS grc_audit_findings (
    id BIGSERIAL PRIMARY KEY,
    engagement_id BIGINT NOT NULL,
    finding_number VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(200) NOT NULL,
    severity VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    description TEXT
);

CREATE TABLE IF NOT EXISTS grc_audit_finding_responses (
    id BIGSERIAL PRIMARY KEY,
    finding_id BIGINT NOT NULL,
    management_response TEXT NOT NULL,
    agreed_remediation_date DATE NOT NULL,
    responder_id BIGINT,
    responded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
