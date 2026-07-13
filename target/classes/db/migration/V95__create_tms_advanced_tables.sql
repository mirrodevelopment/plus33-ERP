-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 95
-- File              : V95__create_tms_advanced_tables.sql
-- Operation Type    : Schema Creation
-- Purpose           : create tms advanced tables
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
-- V95: Advanced TMS Route Optimization, Load Planning & GPS Telemetry Tables
CREATE TABLE IF NOT EXISTS delivery_routes (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    carrier_id BIGINT,
    route_number VARCHAR(50) NOT NULL UNIQUE,
    driver_name VARCHAR(100),
    vehicle_number VARCHAR(50),
    max_capacity_kg NUMERIC(12, 3),
    current_load_kg NUMERIC(12, 3) DEFAULT 0,
    status VARCHAR(30) NOT NULL DEFAULT 'PLANNED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS route_stops (
    id BIGSERIAL PRIMARY KEY,
    route_id BIGINT NOT NULL REFERENCES delivery_routes(id),
    stop_sequence INT NOT NULL,
    shipment_id BIGINT NOT NULL,
    estimated_arrival TIMESTAMP,
    actual_arrival TIMESTAMP,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING'
);

CREATE TABLE IF NOT EXISTS gps_tracking_logs (
    id BIGSERIAL PRIMARY KEY,
    route_id BIGINT NOT NULL REFERENCES delivery_routes(id),
    latitude NUMERIC(10, 6) NOT NULL,
    longitude NUMERIC(10, 6) NOT NULL,
    speed_kmh NUMERIC(5, 2),
    logged_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
