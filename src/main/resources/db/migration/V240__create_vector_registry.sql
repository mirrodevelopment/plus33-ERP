-- V240: Embedding/vector registry
CREATE TABLE IF NOT EXISTS platform_vector_store (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    store_code          VARCHAR(100) NOT NULL UNIQUE, -- PGVECTOR, MILVUS, QDRANT
    status              VARCHAR(50) NOT NULL DEFAULT 'CONNECTED'
);

CREATE TABLE IF NOT EXISTS platform_embedding_provider (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    provider_name       VARCHAR(100) NOT NULL UNIQUE, -- GEMINI, OPENAI, AZURE
    dimensions          INT NOT NULL DEFAULT 1536
);
