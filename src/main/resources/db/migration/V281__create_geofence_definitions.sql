-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 281
-- File              : V281__create_geofence_definitions.sql
-- Operation Type    : Schema Creation
-- Purpose           : create geofence definitions
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
-- V281: Geofence Definitions
CREATE TABLE IF NOT EXISTS platform_geofence_definition (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    geofence_code       VARCHAR(100) NOT NULL UNIQUE,
    geofence_type       VARCHAR(50) NOT NULL, -- Polygon, Circle, Rectangle, Corridor
    geometry_wkt        TEXT NOT NULL,
    center_lat          NUMERIC(9,6) NOT NULL,
    center_lng          NUMERIC(9,6) NOT NULL,
    radius_meters       NUMERIC(10,2),
    altitude_min        NUMERIC(7,2),
    altitude_max        NUMERIC(7,2),
    timezone            VARCHAR(100) NOT NULL,
    active_from         TIMESTAMP,
    active_until        TIMESTAMP,
    tenant_id           VARCHAR(100) NOT NULL DEFAULT 'DEFAULT_TENANT'
);
