-- V148: Succession Planning
CREATE TABLE IF NOT EXISTS hcm_talent_pools (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE IF NOT EXISTS hcm_successors (
    id BIGSERIAL PRIMARY KEY,
    talent_pool_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    readiness_score NUMERIC(5, 2) NOT NULL DEFAULT 0.00
);
