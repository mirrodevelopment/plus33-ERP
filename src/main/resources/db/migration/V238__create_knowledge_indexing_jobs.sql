-- V238: Knowledge indexing & refresh jobs
CREATE TABLE IF NOT EXISTS platform_knowledge_version (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    source_id           BIGINT NOT NULL,
    index_version       VARCHAR(50) NOT NULL,
    total_chunks        INT NOT NULL,
    indexed_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_knowledge_refresh_job (
    id                  BIGSERIAL PRIMARY KEY,
    source_id           BIGINT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- RUNNING, SUCCESS, FAILED
    logs                TEXT,
    triggered_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
