-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 372
-- File              : V372__create_esg_scope2.sql
-- Operation Type    : Schema Creation
-- Purpose           : create esg scope2
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
-- V372: Scope 2 Indirect Emissions
CREATE TABLE IF NOT EXISTS platform_esg_scope2_log (
    id                          BIGSERIAL PRIMARY KEY,
    vehicle_id                  BIGINT NOT NULL,
    charging_station_id         BIGINT NOT NULL,
    energy_kwh                  NUMERIC(10,2) NOT NULL,
    grid_region                 VARCHAR(100) NOT NULL,
    grid_factor                 NUMERIC(10,4) NOT NULL,
    renewable_percentage        NUMERIC(5,2) NOT NULL,
    market_based_co2e_kg        NUMERIC(10,2) NOT NULL,
    location_based_co2e_kg      NUMERIC(10,2) NOT NULL,
    logged_at                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
