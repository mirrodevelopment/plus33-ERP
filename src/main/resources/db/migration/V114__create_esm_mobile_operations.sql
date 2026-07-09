-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 114
-- File              : V114__create_esm_mobile_operations.sql
-- Operation Type    : Schema Creation
-- Purpose           : create esm mobile operations
--
-- Tables Created    : IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V114: Mobile Operations, Sessions, GPS tracking, and Survey Feedback
CREATE TABLE IF NOT EXISTS esm_technician_sessions (
    id BIGSERIAL PRIMARY KEY,
    technician_id BIGINT NOT NULL,
    device_id VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    last_gps_latitude NUMERIC(10, 6),
    last_gps_longitude NUMERIC(10, 6),
    logged_in_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS esm_surveys (
    id BIGSERIAL PRIMARY KEY,
    work_order_id BIGINT NOT NULL UNIQUE,
    csat_score INT NOT NULL,
    nps_score INT NOT NULL,
    ces_score INT NOT NULL,
    comments TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
