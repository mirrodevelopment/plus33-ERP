-- V237: Prompt versioning
CREATE TABLE IF NOT EXISTS platform_agent_prompt (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    prompt_code         VARCHAR(100) NOT NULL UNIQUE,
    description         TEXT
);

CREATE TABLE IF NOT EXISTS platform_prompt_version (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    prompt_id           BIGINT NOT NULL,
    prompt_version      VARCHAR(50) NOT NULL,
    system_prompt       TEXT NOT NULL,
    user_template       TEXT NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
