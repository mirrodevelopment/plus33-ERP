-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 244
-- File              : V244__create_telemetry_thresholds.sql
-- Operation Type    : Schema Creation
-- Purpose           : create telemetry thresholds
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
-- V244: Telemetry & anomaly thresholds
CREATE TABLE IF NOT EXISTS platform_twin_telemetry (
    id                  BIGSERIAL PRIMARY KEY,
    instance_id         BIGINT NOT NULL,
    metric_name         VARCHAR(100) NOT NULL,
    metric_value        NUMERIC(19,4) NOT NULL,
    recorded_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_twin_anomaly_threshold (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    instance_id         BIGINT NOT NULL,
    metric_name         VARCHAR(100) NOT NULL,
    min_value           NUMERIC(19,4) NOT NULL,
    max_value           NUMERIC(19,4) NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_twin_alert (
    id                  BIGSERIAL PRIMARY KEY,
    instance_id         BIGINT NOT NULL,
    alert_type          VARCHAR(100) NOT NULL,
    severity            VARCHAR(50) NOT NULL DEFAULT 'WARNING',
    message             TEXT NOT NULL,
    triggered_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
