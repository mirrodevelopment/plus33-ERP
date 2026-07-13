-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 332
-- File              : V332__create_carbon_footprints.sql
-- Operation Type    : Schema Creation
-- Purpose           : create carbon footprints
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
-- V332: Carbon Footprints
CREATE TABLE IF NOT EXISTS platform_carbon_footprint_log (
    id                      BIGSERIAL PRIMARY KEY,
    vehicle_id              BIGINT NOT NULL,
    route_id                BIGINT NOT NULL,
    fuel_type               VARCHAR(50) NOT NULL, -- Diesel, Petrol, Electric, Hybrid
    fuel_consumption_liters NUMERIC(10,2) NOT NULL,
    co2_kg                  NUMERIC(10,2) NOT NULL,
    co2e_kg                 NUMERIC(10,2) NOT NULL,
    nox_g                   NUMERIC(10,2) NOT NULL,
    pm10_g                  NUMERIC(10,2) NOT NULL,
    distance_km             NUMERIC(10,2) NOT NULL,
    idle_time_mins          INT NOT NULL,
    average_speed_kmh       NUMERIC(5,2) NOT NULL,
    calculation_method      VARCHAR(100) NOT NULL,
    emission_factor_version VARCHAR(50) NOT NULL,
    logged_at               TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
