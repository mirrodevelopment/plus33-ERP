-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 343
-- File              : V343__create_route_simulations.sql
-- Operation Type    : Schema Creation
-- Purpose           : create route simulations
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
-- V343: Route Simulations
CREATE TABLE IF NOT EXISTS platform_route_simulation_run (
    id                          BIGSERIAL PRIMARY KEY,
    scenario_name               VARCHAR(200) NOT NULL,
    baseline_route              TEXT NOT NULL,
    optimized_route             TEXT NOT NULL,
    travel_time_mins            INT NOT NULL,
    fuel_cost                   NUMERIC(12,2) NOT NULL,
    carbon_output_kg            NUMERIC(10,2) NOT NULL,
    delay_probability           NUMERIC(5,2) NOT NULL,
    simulation_score            NUMERIC(5,2) NOT NULL,
    algorithm_version           VARCHAR(50) NOT NULL,
    simulated_at                TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
