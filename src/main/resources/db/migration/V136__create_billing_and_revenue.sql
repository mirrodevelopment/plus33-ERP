-- V136: Project Contracts and Billing milestones
CREATE TABLE IF NOT EXISTS project_billing_contracts (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL UNIQUE,
    contract_type VARCHAR(30) NOT NULL DEFAULT 'TIME_AND_MATERIAL',
    billing_amount NUMERIC(18, 2) NOT NULL DEFAULT 0,
    recognized_revenue NUMERIC(18, 2) NOT NULL DEFAULT 0,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT'
);

CREATE TABLE IF NOT EXISTS project_billing_milestones (
    id BIGSERIAL PRIMARY KEY,
    contract_id BIGINT NOT NULL,
    milestone_name VARCHAR(100) NOT NULL,
    milestone_percentage NUMERIC(5, 2) NOT NULL,
    amount NUMERIC(18, 2) NOT NULL,
    billed BOOLEAN NOT NULL DEFAULT FALSE
);
