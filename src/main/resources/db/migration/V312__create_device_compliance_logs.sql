-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 312
-- File              : V312__create_device_compliance_logs.sql
-- Operation Type    : Schema Creation
-- Purpose           : create device compliance logs
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
-- V312: Compliance & Drift Logs
CREATE TABLE IF NOT EXISTS platform_device_compliance_log (
    id                      BIGSERIAL PRIMARY KEY,
    device_id               BIGINT NOT NULL,
    policy_id               BIGINT NOT NULL,
    result                  VARCHAR(50) NOT NULL, -- PASS, FAIL, WARNING, UNKNOWN
    execution_time          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    duration_ms             BIGINT NOT NULL,
    details                 TEXT
);

CREATE TABLE IF NOT EXISTS platform_device_drift_log (
    id                      BIGSERIAL PRIMARY KEY,
    device_id               BIGINT NOT NULL,
    baseline_hash           VARCHAR(64) NOT NULL,
    current_hash            VARCHAR(64) NOT NULL,
    changed_files           TEXT,
    registry_changes        TEXT,
    package_changes         TEXT,
    service_changes         TEXT,
    detected_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
