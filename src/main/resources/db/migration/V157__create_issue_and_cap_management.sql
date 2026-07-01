-- V157: Enterprise Issue & CAP Management
CREATE TABLE IF NOT EXISTS grc_enterprise_issues (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    issue_number VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(200) NOT NULL,
    source VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    severity VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    owner_id BIGINT,
    due_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS grc_corrective_action_plans (
    id BIGSERIAL PRIMARY KEY,
    issue_id BIGINT NOT NULL,
    description TEXT NOT NULL,
    owner_id BIGINT,
    due_date DATE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    closed_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS grc_cap_tasks (
    id BIGSERIAL PRIMARY KEY,
    cap_id BIGINT NOT NULL,
    task_description TEXT NOT NULL,
    owner_id BIGINT,
    due_date DATE NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE
);
