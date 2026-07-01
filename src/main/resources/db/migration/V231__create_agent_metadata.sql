-- V231: Agent registry & prompt metadata
CREATE TABLE IF NOT EXISTS platform_ai_agent (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    agent_code          VARCHAR(100) NOT NULL UNIQUE,
    agent_name          VARCHAR(150) NOT NULL,
    system_instruction  TEXT NOT NULL,
    active              BOOLEAN NOT NULL DEFAULT TRUE
);
