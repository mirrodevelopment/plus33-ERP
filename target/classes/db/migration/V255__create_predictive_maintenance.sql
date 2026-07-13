-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 255
-- File              : V255__create_predictive_maintenance.sql
-- Operation Type    : Schema Creation
-- Purpose           : create predictive maintenance
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
-- V255: Predictive Maintenance
CREATE TABLE IF NOT EXISTS platform_forecast_model_registry (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    model_code          VARCHAR(100) NOT NULL UNIQUE,
    accuracy_score      NUMERIC(5,2) NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS platform_predictive_maintenance_forecast (
    id                  BIGSERIAL PRIMARY KEY,
    model_id            BIGINT NOT NULL,
    twin_instance_id    BIGINT NOT NULL,
    failure_probability NUMERIC(5,2) NOT NULL,
    expected_failure_at TIMESTAMP NOT NULL,
    generated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
