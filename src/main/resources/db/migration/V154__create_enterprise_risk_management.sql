-- V154: Enterprise Risk Management
CREATE TABLE IF NOT EXISTS grc_risk_appetite_statements (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    risk_category VARCHAR(50) NOT NULL,
    max_residual_score NUMERIC(5, 2) NOT NULL,
    escalation_threshold NUMERIC(5, 2) NOT NULL,
    approval_authority VARCHAR(100) NOT NULL,
    effective_from DATE NOT NULL,
    effective_to DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS grc_risks (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    risk_number VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL,
    domain VARCHAR(100),
    inherent_score NUMERIC(5, 2) NOT NULL DEFAULT 0.00,
    residual_score NUMERIC(5, 2) NOT NULL DEFAULT 0.00,
    status VARCHAR(30) NOT NULL DEFAULT 'IDENTIFIED',
    owner_employee_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS grc_risk_assessments (
    id BIGSERIAL PRIMARY KEY,
    risk_id BIGINT NOT NULL,
    probability NUMERIC(5, 2) NOT NULL,
    impact NUMERIC(5, 2) NOT NULL,
    residual_score NUMERIC(5, 2) NOT NULL,
    assessment_date DATE NOT NULL,
    assessor_id BIGINT
);

CREATE TABLE IF NOT EXISTS grc_risk_mitigation_plans (
    id BIGSERIAL PRIMARY KEY,
    risk_id BIGINT NOT NULL,
    description TEXT NOT NULL,
    owner_id BIGINT,
    due_date DATE NOT NULL,
    effectiveness_rating VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS grc_risk_kris (
    id BIGSERIAL PRIMARY KEY,
    risk_id BIGINT NOT NULL,
    indicator_name VARCHAR(150) NOT NULL,
    threshold_value NUMERIC(12, 4) NOT NULL,
    current_value NUMERIC(12, 4),
    breached BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS grc_risk_treatment_history (
    id BIGSERIAL PRIMARY KEY,
    risk_id BIGINT NOT NULL,
    from_status VARCHAR(30) NOT NULL,
    to_status VARCHAR(30) NOT NULL,
    decision TEXT,
    decided_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
