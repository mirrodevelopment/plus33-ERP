-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 234
-- File              : V234__create_knowledge_sources.sql
-- Operation Type    : Schema Creation
-- Purpose           : create knowledge sources
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
