-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 263
-- File              : V263__create_delay_predictions.sql
-- Operation Type    : Schema Creation
-- Purpose           : create delay predictions
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
-- V263: Delay Prediction
CREATE TABLE IF NOT EXISTS platform_logistics_delay_prediction (
    id                  BIGSERIAL PRIMARY KEY,
    transit_route_id    BIGINT NOT NULL,
    prediction_model    VARCHAR(100) NOT NULL,
    prediction_confidence NUMERIC(5,2) NOT NULL,
    predicted_arrival   TIMESTAMP NOT NULL,
    actual_arrival      TIMESTAMP,
    generated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
