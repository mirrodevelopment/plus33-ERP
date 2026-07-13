-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 240
-- File              : V240__create_vector_registry.sql
-- Operation Type    : Schema Creation
-- Purpose           : create vector registry
--
-- Tables Created    : IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
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
