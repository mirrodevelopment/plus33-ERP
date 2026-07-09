-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 273
-- File              : V273__create_machinery_telemetry.sql
-- Operation Type    : Schema Creation
-- Purpose           : create machinery telemetry
--
-- Tables Created    : IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : IF
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V273: Machinery Telemetry
CREATE TABLE IF NOT EXISTS platform_machinery_telemetry (
    id                  BIGSERIAL PRIMARY KEY,
    device_id           BIGINT NOT NULL,
    signal_id           BIGINT NOT NULL,
    recorded_at         TIMESTAMP NOT NULL,
    quality             VARCHAR(50) NOT NULL, -- GOOD, BAD, UNCERTAIN
    value               NUMERIC(19,4) NOT NULL,
    unit                VARCHAR(50) NOT NULL,
    sequence_num        BIGINT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_machinery_telemetry_time ON platform_machinery_telemetry (recorded_at DESC);
