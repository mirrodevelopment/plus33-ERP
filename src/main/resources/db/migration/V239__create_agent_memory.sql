-- V239: Agent memory & context storage
CREATE TABLE IF NOT EXISTS platform_agent_memory (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    session_id          BIGINT NOT NULL,
    memory_scope        VARCHAR(50) NOT NULL, -- SESSION, USER, ORG
    memory_key          VARCHAR(150) NOT NULL,
    memory_value        TEXT NOT NULL,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
