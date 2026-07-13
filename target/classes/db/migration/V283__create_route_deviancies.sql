-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 283
-- File              : V283__create_route_deviancies.sql
-- Operation Type    : Schema Creation
-- Purpose           : create route deviancies
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
-- V283: Route Deviancies
CREATE TABLE IF NOT EXISTS platform_route_deviancy_log (
    id                  BIGSERIAL PRIMARY KEY,
    transit_route_id    BIGINT NOT NULL,
    expected_route_wkt  TEXT NOT NULL,
    actual_route_wkt    TEXT NOT NULL,
    deviation_distance  NUMERIC(10,2) NOT NULL,
    deviation_duration_minutes INT NOT NULL,
    severity            VARCHAR(50) NOT NULL, -- Low, Medium, High
    automatic_recovery  BOOLEAN NOT NULL DEFAULT FALSE,
    reroute_triggered   BOOLEAN NOT NULL DEFAULT FALSE,
    detected_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
