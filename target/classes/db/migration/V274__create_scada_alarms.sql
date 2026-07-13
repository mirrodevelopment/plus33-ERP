-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 274
-- File              : V274__create_scada_alarms.sql
-- Operation Type    : Schema Creation
-- Purpose           : create scada alarms
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
-- V274: SCADA Alarms
CREATE TABLE IF NOT EXISTS platform_scada_alarm_policy (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    policy_code         VARCHAR(100) NOT NULL UNIQUE,
    severity            VARCHAR(50) NOT NULL, -- Critical, High, Medium, Low
    threshold_value     NUMERIC(19,4) NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_scada_alarm_event (
    id                  BIGSERIAL PRIMARY KEY,
    device_id           BIGINT NOT NULL,
    policy_id           BIGINT NOT NULL,
    alarm_status        VARCHAR(50) NOT NULL DEFAULT 'ACTIVE', -- Acknowledged, Shelved, Suppressed, Returned To Normal
    message             TEXT NOT NULL,
    triggered_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at         TIMESTAMP
);
