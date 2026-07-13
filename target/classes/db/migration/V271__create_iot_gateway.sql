-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 271
-- File              : V271__create_iot_gateway.sql
-- Operation Type    : Schema Creation
-- Purpose           : create iot gateway
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
-- V271: IoT Gateway Registry & Heartbeats
CREATE TABLE IF NOT EXISTS platform_iot_gateway (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    gateway_code        VARCHAR(100) NOT NULL UNIQUE,
    gateway_status      VARCHAR(50) NOT NULL DEFAULT 'OFFLINE',
    firmware_version    VARCHAR(50) NOT NULL,
    certificate_thumbprint VARCHAR(150) NOT NULL,
    edge_cluster        VARCHAR(100) NOT NULL,
    mqtt_client_id      VARCHAR(100) NOT NULL UNIQUE,
    last_seen           TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_gateway_heartbeat (
    id                  BIGSERIAL PRIMARY KEY,
    gateway_id          BIGINT NOT NULL,
    recorded_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
