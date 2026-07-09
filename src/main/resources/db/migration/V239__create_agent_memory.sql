-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 239
-- File              : V239__create_agent_memory.sql
-- Operation Type    : Schema Creation
-- Purpose           : create agent memory
--
-- Tables Created    : IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
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
