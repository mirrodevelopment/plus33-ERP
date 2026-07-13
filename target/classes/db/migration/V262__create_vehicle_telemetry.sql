-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 262
-- File              : V262__create_vehicle_telemetry.sql
-- Operation Type    : Schema Creation
-- Purpose           : create vehicle telemetry
--
-- Tables Created    : IF, IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V262: Vehicle Telemetry
CREATE TABLE IF NOT EXISTS platform_vehicle (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    vehicle_code        VARCHAR(100) NOT NULL UNIQUE,
    capacity_kg         NUMERIC(10,2) NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE'
);

CREATE TABLE IF NOT EXISTS platform_vehicle_telemetry (
    id                  BIGSERIAL PRIMARY KEY,
    vehicle_id          BIGINT NOT NULL,
    latitude            NUMERIC(9,6) NOT NULL,
    longitude           NUMERIC(9,6) NOT NULL,
    speed_kmh           NUMERIC(5,2) NOT NULL,
    recorded_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_transit_route (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    vehicle_id          BIGINT NOT NULL,
    origin_node_id      BIGINT NOT NULL,
    dest_node_id        BIGINT NOT NULL,
    route_path_json     TEXT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'IN_TRANSIT'
);
