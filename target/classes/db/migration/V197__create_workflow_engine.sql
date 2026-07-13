-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 197
-- File              : V197__create_workflow_engine.sql
-- Operation Type    : Schema Creation
-- Purpose           : create workflow engine
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
-- V197: Workflow Engine DDL
CREATE TABLE IF NOT EXISTS integration_workflow_definition (
    id                  BIGSERIAL PRIMARY KEY,
    definition_code     VARCHAR(100) NOT NULL UNIQUE,
    name                VARCHAR(250) NOT NULL,
    layout_json         TEXT NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS integration_workflow_instance (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT DEFAULT 0,
    definition_code     VARCHAR(100) NOT NULL,
    instance_code       VARCHAR(100) NOT NULL UNIQUE,
    status              VARCHAR(50) NOT NULL DEFAULT 'RUNNING',
    current_step        VARCHAR(100),
    variables_json      TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS integration_workflow_task (
    id                  BIGSERIAL PRIMARY KEY,
    instance_code       VARCHAR(100) NOT NULL,
    task_name           VARCHAR(100) NOT NULL,
    status              VARCHAR(50) NOT NULL,
    attempts            INT NOT NULL DEFAULT 0,
    started_at          TIMESTAMP NOT NULL,
    completed_at        TIMESTAMP
);
