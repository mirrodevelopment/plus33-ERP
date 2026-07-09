-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 232
-- File              : V232__create_agent_conversations.sql
-- Operation Type    : Schema Creation
-- Purpose           : create agent conversations
--
-- Tables Created    : IF, IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V232: Sessions, conversations & reasoning
CREATE TABLE IF NOT EXISTS platform_agent_session (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    session_token       VARCHAR(100) NOT NULL UNIQUE,
    user_identity       VARCHAR(100) NOT NULL,
    active              BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_agent_message (
    id                  BIGSERIAL PRIMARY KEY,
    session_id          BIGINT NOT NULL,
    sender_role         VARCHAR(50) NOT NULL, -- USER, AGENT, SYSTEM
    content             TEXT NOT NULL,
    sent_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_agent_reasoning (
    id                  BIGSERIAL PRIMARY KEY,
    message_id          BIGINT NOT NULL,
    thought_process     TEXT NOT NULL,
    confidence_score    NUMERIC(5,2) NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
