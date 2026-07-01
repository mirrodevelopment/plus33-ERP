-- V134: Project Budgeting and Forecasts
CREATE TABLE IF NOT EXISTS project_budgets (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    current_version INT NOT NULL DEFAULT 1,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT'
);

CREATE TABLE IF NOT EXISTS project_budget_versions (
    id BIGSERIAL PRIMARY KEY,
    budget_id BIGINT NOT NULL,
    version_number INT NOT NULL,
    original_budget_amount NUMERIC(18, 2) NOT NULL DEFAULT 0,
    revised_budget_amount NUMERIC(18, 2) NOT NULL DEFAULT 0,
    forecast_amount NUMERIC(18, 2) NOT NULL DEFAULT 0,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    UNIQUE(budget_id, version_number)
);
