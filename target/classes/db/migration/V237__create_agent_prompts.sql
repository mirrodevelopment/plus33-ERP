-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 237
-- File              : V237__create_agent_prompts.sql
-- Operation Type    : Schema Creation
-- Purpose           : create agent prompts
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
