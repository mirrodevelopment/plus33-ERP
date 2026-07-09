-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 325
-- File              : V325__create_predictive_audits.sql
-- Operation Type    : Schema Creation
-- Purpose           : create predictive audits
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
-- V325: Predictive Audit Logs
CREATE TABLE IF NOT EXISTS platform_predictive_audit_log (
    id                              BIGSERIAL PRIMARY KEY,
    prediction_id                   BIGINT NOT NULL,
    operator                        VARCHAR(100) NOT NULL,
    action_type                     VARCHAR(100) NOT NULL, -- UPDATE_THRESHOLD, MODEL_REDEPLOY, STRATEGY_CHANGE
    previous_threshold_config       TEXT,
    new_threshold_config            TEXT,
    approval_reference              VARCHAR(100),
    trace_id                        VARCHAR(100) NOT NULL,
    reason                          VARCHAR(500),
    audited_at                      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
