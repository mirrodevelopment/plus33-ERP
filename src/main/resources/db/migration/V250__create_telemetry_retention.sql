-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 250
-- File              : V250__create_telemetry_retention.sql
-- Operation Type    : Schema Creation
-- Purpose           : create telemetry retention
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
-- V250: Telemetry retention & archival
CREATE TABLE IF NOT EXISTS platform_telemetry_retention_policy (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    instance_id         BIGINT NOT NULL UNIQUE,
    retention_days      INT NOT NULL DEFAULT 30,
    archival_target     VARCHAR(150) NOT NULL DEFAULT 'S3_COLD'
);

CREATE TABLE IF NOT EXISTS platform_telemetry_archive_log (
    id                  BIGSERIAL PRIMARY KEY,
    instance_id         BIGINT NOT NULL,
    records_archived    INT NOT NULL,
    archive_key         VARCHAR(250) NOT NULL,
    archived_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
