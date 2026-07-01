-- V137: Portfolios and Programs Governance
CREATE TABLE IF NOT EXISTS project_portfolios (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS project_programs (
    id BIGSERIAL PRIMARY KEY,
    portfolio_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL
);

ALTER TABLE projects ADD COLUMN IF NOT EXISTS program_id BIGINT;
