-- V234: Knowledge sources, chunks & vector metadata
CREATE TABLE IF NOT EXISTS platform_knowledge_source (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    source_name         VARCHAR(150) NOT NULL UNIQUE,
    source_type         VARCHAR(100) NOT NULL, -- PDF, DATABASE, WIKI
    active              BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS platform_knowledge_chunk (
    id                  BIGSERIAL PRIMARY KEY,
    source_id           BIGINT NOT NULL,
    chunk_content       TEXT NOT NULL,
    vector_placeholder  VARCHAR(250)
);
