-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 352
-- File              : V352__create_eco_driving.sql
-- Operation Type    : Schema Creation
-- Purpose           : create eco driving
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
-- V352: Eco-Driving Diagnostics
CREATE TABLE IF NOT EXISTS platform_eco_driving_log (
    id                          BIGSERIAL PRIMARY KEY,
    driver_id                   BIGINT NOT NULL,
    trip_id                     BIGINT NOT NULL,
    harsh_acceleration_count    INT NOT NULL,
    harsh_braking_count         INT NOT NULL,
    harsh_cornering_count       INT NOT NULL,
    excessive_idle_seconds      INT NOT NULL,
    overspeed_duration_secs     INT NOT NULL,
    cruise_control_usage_pct    NUMERIC(5,2) NOT NULL,
    driver_score                NUMERIC(5,2) NOT NULL,
    trip_score                  NUMERIC(5,2) NOT NULL,
    coaching_status             VARCHAR(50) NOT NULL, -- OK, NEEDS_COACHING, COACHED
    logged_at                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
