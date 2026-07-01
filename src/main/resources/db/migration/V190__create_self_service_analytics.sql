-- V190: Self-Service Personal Workspace and Custom Measures Schema
CREATE TABLE IF NOT EXISTS bi_self_service_workspace (
    id                  BIGSERIAL PRIMARY KEY,
    workspace_code      VARCHAR(100) NOT NULL UNIQUE,
    owner_user          VARCHAR(100) NOT NULL,
    company_id          BIGINT NOT NULL,
    workspace_name      VARCHAR(150) NOT NULL,
    config_json         TEXT NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);