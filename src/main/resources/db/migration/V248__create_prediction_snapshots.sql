-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 248
-- File              : V248__create_prediction_snapshots.sql
-- Operation Type    : Schema Creation
-- Purpose           : create prediction snapshots
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
-- V248: Prediction snapshots
CREATE TABLE IF NOT EXISTS platform_twin_prediction_snapshot (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    instance_id         BIGINT NOT NULL,
    target_time         TIMESTAMP NOT NULL,
    predicted_state_json TEXT NOT NULL,
    confidence          NUMERIC(5,2) NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
