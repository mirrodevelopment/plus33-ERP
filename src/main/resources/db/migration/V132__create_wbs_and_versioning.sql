-- V132: WBS Versioning and Tasks
CREATE TABLE IF NOT EXISTS project_wbs (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    current_version INT NOT NULL DEFAULT 1,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT'
);

CREATE TABLE IF NOT EXISTS project_wbs_versions (
    id BIGSERIAL PRIMARY KEY,
    wbs_id BIGINT NOT NULL,
    version_number INT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    effective_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(wbs_id, version_number)
);

CREATE TABLE IF NOT EXISTS project_tasks (
    id BIGSERIAL PRIMARY KEY,
    wbs_version_id BIGINT NOT NULL,
    task_number VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    estimated_hours NUMERIC(10, 2) NOT NULL DEFAULT 0,
    actual_hours NUMERIC(10, 2) NOT NULL DEFAULT 0,
    start_date DATE,
    end_date DATE,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING'
);

CREATE TABLE IF NOT EXISTS project_task_dependencies (
    id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL,
    predecessor_task_id BIGINT NOT NULL,
    dependency_type VARCHAR(20) NOT NULL DEFAULT 'FS',
    lag_days INT NOT NULL DEFAULT 0,
    lead_days INT NOT NULL DEFAULT 0
);
