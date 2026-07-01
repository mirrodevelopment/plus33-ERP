-- V138: Project Risks and Change Management
CREATE TABLE IF NOT EXISTS project_risks (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    probability VARCHAR(20) NOT NULL DEFAULT 'LOW',
    impact VARCHAR(20) NOT NULL DEFAULT 'LOW',
    mitigation_plan TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS project_change_requests (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    request_number VARCHAR(50) NOT NULL UNIQUE,
    change_type VARCHAR(30) NOT NULL, -- SCOPE, BUDGET, SCHEDULE
    impact_analysis TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING'
);
