-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 361
-- File              : V361__create_ev_telemetry.sql
-- Operation Type    : Schema Creation
-- Purpose           : create ev telemetry
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
-- V361: EV Battery Telemetry Ingestion
CREATE TABLE IF NOT EXISTS platform_ev_telemetry_log (
    id                          BIGSERIAL PRIMARY KEY,
    vehicle_id                  BIGINT NOT NULL,
    battery_pack_id             VARCHAR(100) NOT NULL,
    state_of_charge_pct         NUMERIC(5,2) NOT NULL,
    state_of_health_pct         NUMERIC(5,2) NOT NULL,
    battery_voltage             NUMERIC(7,2) NOT NULL,
    battery_current             NUMERIC(7,2) NOT NULL,
    battery_temperature_c       NUMERIC(5,2) NOT NULL,
    charging_power_kw           NUMERIC(7,2) NOT NULL,
    discharge_power_kw          NUMERIC(7,2) NOT NULL,
    regenerative_power_kw       NUMERIC(7,2) NOT NULL,
    remaining_range_km          NUMERIC(10,2) NOT NULL,
    energy_consumed_kwh         NUMERIC(10,2) NOT NULL,
    energy_regenerated_kwh      NUMERIC(10,2) NOT NULL,
    odometer_km                 NUMERIC(12,2) NOT NULL,
    gps_latitude                NUMERIC(10,6) NOT NULL,
    gps_longitude               NUMERIC(10,6) NOT NULL,
    logged_at                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
