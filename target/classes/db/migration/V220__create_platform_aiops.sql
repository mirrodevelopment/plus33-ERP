-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 220
-- File              : V220__create_platform_aiops.sql
-- Operation Type    : Schema Creation
-- Purpose           : create platform aiops
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
-- V220: AIOps DDL
CREATE TABLE IF NOT EXISTS platform_aiops_model (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    model_name          VARCHAR(150) NOT NULL UNIQUE,
    accuracy            NUMERIC(5,2) NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS platform_capacity_forecast (
    id                  BIGSERIAL PRIMARY KEY,
    metric_name         VARCHAR(100) NOT NULL,
    forecast_value      NUMERIC(19,4) NOT NULL,
    confidence          NUMERIC(5,2) NOT NULL,
    target_time         TIMESTAMP NOT NULL
);
