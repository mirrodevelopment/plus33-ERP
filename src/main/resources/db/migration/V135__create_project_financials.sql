-- V135: Project Costing and Financial Ledger
CREATE TABLE IF NOT EXISTS project_cost_ledger (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    task_id BIGINT,
    cost_type VARCHAR(30) NOT NULL, -- LABOR, MATERIAL, OVERHEAD, etc.
    amount NUMERIC(18, 2) NOT NULL,
    source_module VARCHAR(50),
    source_id BIGINT,
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
