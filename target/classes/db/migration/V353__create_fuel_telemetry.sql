-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 353
-- File              : V353__create_fuel_telemetry.sql
-- Operation Type    : Schema Creation
-- Purpose           : create fuel telemetry
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
-- V353: Fuel Telemetry
CREATE TABLE IF NOT EXISTS platform_fuel_telemetry_log (
    id                          BIGSERIAL PRIMARY KEY,
    vehicle_id                  BIGINT NOT NULL,
    gateway_id                  BIGINT NOT NULL,
    engine_rpm                  NUMERIC(7,2) NOT NULL,
    throttle_position_pct       NUMERIC(5,2) NOT NULL,
    instantaneous_fuel_rate     NUMERIC(10,2) NOT NULL,
    average_fuel_rate           NUMERIC(10,2) NOT NULL,
    fuel_level_pct              NUMERIC(5,2) NOT NULL,
    coolant_temperature_c       NUMERIC(5,2) NOT NULL,
    engine_load_pct             NUMERIC(5,2) NOT NULL,
    odometer_km                 NUMERIC(12,2) NOT NULL,
    trip_distance_km            NUMERIC(10,2) NOT NULL,
    idle_time_seconds           INT NOT NULL,
    gps_latitude                NUMERIC(10,6) NOT NULL,
    gps_longitude               NUMERIC(10,6) NOT NULL,
    logged_at                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
