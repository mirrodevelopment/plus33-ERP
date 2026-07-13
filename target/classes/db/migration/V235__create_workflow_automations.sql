-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 235
-- File              : V235__create_workflow_automations.sql
-- Operation Type    : Schema Creation
-- Purpose           : create workflow automations
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
-- V235: Workflow automation & orchestration
CREATE TABLE IF NOT EXISTS platform_agent_workflow (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    workflow_code       VARCHAR(100) NOT NULL UNIQUE,
    workflow_name       VARCHAR(150) NOT NULL,
    active              BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS platform_agent_workflow_run (
    id                  BIGSERIAL PRIMARY KEY,
    workflow_id         BIGINT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- RUNNING, COMPLETED, FAILED
    started_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at        TIMESTAMP
);
