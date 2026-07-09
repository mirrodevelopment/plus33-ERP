-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 231
-- File              : V231__create_agent_metadata.sql
-- Operation Type    : Schema Creation
-- Purpose           : create agent metadata
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
-- V231: Agent registry & prompt metadata
CREATE TABLE IF NOT EXISTS platform_ai_agent (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    agent_code          VARCHAR(100) NOT NULL UNIQUE,
    agent_name          VARCHAR(150) NOT NULL,
    system_instruction  TEXT NOT NULL,
    active              BOOLEAN NOT NULL DEFAULT TRUE
);
