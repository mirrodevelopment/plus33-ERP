-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 254
-- File              : V254__create_root_cause_analysis.sql
-- Operation Type    : Schema Creation
-- Purpose           : create root cause analysis
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
-- V254: Root Cause Analysis
CREATE TABLE IF NOT EXISTS platform_root_cause_analysis (
    id                  BIGSERIAL PRIMARY KEY,
    causal_model_id     BIGINT NOT NULL,
    anomaly_event       VARCHAR(150) NOT NULL,
    probabilities_json  TEXT NOT NULL,
    root_cause_node     VARCHAR(150) NOT NULL,
    explanation         TEXT NOT NULL,
    analyzed_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
