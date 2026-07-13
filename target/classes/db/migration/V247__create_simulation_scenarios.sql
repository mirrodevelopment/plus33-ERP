-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 247
-- File              : V247__create_simulation_scenarios.sql
-- Operation Type    : Schema Creation
-- Purpose           : create simulation scenarios
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
-- V247: Simulation scenarios
CREATE TABLE IF NOT EXISTS platform_twin_simulation_scenario (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    scenario_code       VARCHAR(100) NOT NULL UNIQUE,
    scenario_name       VARCHAR(150) NOT NULL,
    config_variables    TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_twin_simulation_result (
    id                  BIGSERIAL PRIMARY KEY,
    scenario_id         BIGINT NOT NULL,
    simulation_output   TEXT NOT NULL,
    confidence          NUMERIC(5,2) NOT NULL,
    simulated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
