-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 259
-- File              : V259__create_operational_query_logs.sql
-- Operation Type    : Schema Creation
-- Purpose           : create operational query logs
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
-- V259: Operational Query Logs
CREATE TABLE IF NOT EXISTS platform_operational_query_log (
    id                  BIGSERIAL PRIMARY KEY,
    query_text          TEXT NOT NULL,
    parsed_intent       VARCHAR(250) NOT NULL,
    execution_plan_json TEXT NOT NULL,
    queried_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
