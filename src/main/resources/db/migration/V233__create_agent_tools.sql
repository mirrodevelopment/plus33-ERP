-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 233
-- File              : V233__create_agent_tools.sql
-- Operation Type    : Schema Creation
-- Purpose           : create agent tools
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
-- V233: Tool registry & execution history
CREATE TABLE IF NOT EXISTS platform_agent_tool (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    tool_code           VARCHAR(100) NOT NULL UNIQUE,
    description         TEXT NOT NULL,
    module_name         VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_agent_tool_execution (
    id                  BIGSERIAL PRIMARY KEY,
    tool_id             BIGINT NOT NULL,
    executor_node       VARCHAR(100) NOT NULL,
    input_parameters    TEXT NOT NULL,
    output_response     TEXT,
    success             BOOLEAN NOT NULL,
    elapsed_ms          BIGINT NOT NULL,
    executed_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
