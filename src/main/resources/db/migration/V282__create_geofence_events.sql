-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 282
-- File              : V282__create_geofence_events.sql
-- Operation Type    : Schema Creation
-- Purpose           : create geofence events
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
-- V282: Geofence Events
CREATE TABLE IF NOT EXISTS platform_geofence_event (
    id                  BIGSERIAL PRIMARY KEY,
    geofence_id         BIGINT NOT NULL,
    asset_id            BIGINT NOT NULL,
    route_id            BIGINT,
    operator_id         VARCHAR(100),
    event_type          VARCHAR(50) NOT NULL, -- ENTER, EXIT, DWELL, INSIDE, OUTSIDE, VIOLATION
    latitude            NUMERIC(9,6) NOT NULL,
    longitude           NUMERIC(9,6) NOT NULL,
    speed_kmh           NUMERIC(5,2),
    heading_degrees     NUMERIC(5,2),
    gps_accuracy_meters NUMERIC(5,2),
    recorded_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
