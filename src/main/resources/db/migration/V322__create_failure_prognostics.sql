-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 322
-- File              : V322__create_failure_prognostics.sql
-- Operation Type    : Schema Creation
-- Purpose           : create failure prognostics
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
-- V322: Failure Prognostics
CREATE TABLE IF NOT EXISTS platform_failure_prognostics_log (
    id                              BIGSERIAL PRIMARY KEY,
    asset_id                        BIGINT NOT NULL,
    prediction_time                 TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    predicted_failure_time          TIMESTAMP,
    remaining_useful_life_hours     NUMERIC(10,2) NOT NULL,
    failure_probability             NUMERIC(5,2) NOT NULL,
    confidence_score                NUMERIC(5,2) NOT NULL,
    prediction_model_version        VARCHAR(50) NOT NULL,
    trigger_reason                  VARCHAR(500),
    recommended_action              VARCHAR(500)
);
