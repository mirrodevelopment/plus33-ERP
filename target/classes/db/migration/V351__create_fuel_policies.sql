-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 351
-- File              : V351__create_fuel_policies.sql
-- Operation Type    : Schema Creation
-- Purpose           : create fuel policies
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
-- V351: Fuel Optimization Policies
CREATE TABLE IF NOT EXISTS platform_fuel_optimization_policy (
    id                          BIGSERIAL PRIMARY KEY,
    version                     INT NOT NULL DEFAULT 0,
    policy_code                 VARCHAR(100) NOT NULL UNIQUE,
    optimization_strategy       VARCHAR(100) NOT NULL, -- LowRPM, EcoSpeed, CargoAware
    vehicle_type                VARCHAR(100) NOT NULL,
    engine_type                 VARCHAR(100) NOT NULL,
    fuel_type                   VARCHAR(50) NOT NULL,
    idle_limit_seconds          INT NOT NULL,
    target_fuel_consumption     NUMERIC(10,2) NOT NULL,
    eco_speed_min               NUMERIC(5,2) NOT NULL,
    eco_speed_max               NUMERIC(5,2) NOT NULL,
    enabled                     BOOLEAN NOT NULL DEFAULT TRUE,
    effective_from              TIMESTAMP NOT NULL,
    effective_to                TIMESTAMP NOT NULL
);
