-- V235: Workflow automation & orchestration
CREATE TABLE IF NOT EXISTS platform_agent_workflow (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    workflow_code       VARCHAR(100) NOT NULL UNIQUE,
    workflow_name       VARCHAR(150) NOT NULL,
    active              BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS platform_agent_workflow_run (
    id                  BIGSERIAL PRIMARY KEY,
    workflow_id         BIGINT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- RUNNING, COMPLETED, FAILED
    started_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at        TIMESTAMP
);
