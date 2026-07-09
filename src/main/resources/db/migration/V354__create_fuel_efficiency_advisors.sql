-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 354
-- File              : V354__create_fuel_efficiency_advisors.sql
-- Operation Type    : Schema Creation
-- Purpose           : create fuel efficiency advisors
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
-- V354: Fuel Efficiency Advisor
CREATE TABLE IF NOT EXISTS platform_fuel_efficiency_advisor (
    id                          BIGSERIAL PRIMARY KEY,
    recommendation_type         VARCHAR(100) NOT NULL, -- EcoSpeedLimit, ReduceIdle, CruiseControl
    priority                    VARCHAR(50) NOT NULL,
    expected_fuel_saving_l      NUMERIC(10,2) NOT NULL,
    expected_cost_saving        NUMERIC(12,2) NOT NULL,
    expected_emission_reduce    NUMERIC(10,2) NOT NULL,
    generated_by                VARCHAR(100) NOT NULL,
    acknowledged                BOOLEAN NOT NULL DEFAULT FALSE,
    applied_at                  TIMESTAMP,
    created_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
