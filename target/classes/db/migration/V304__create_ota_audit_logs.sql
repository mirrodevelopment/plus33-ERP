-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 304
-- File              : V304__create_ota_audit_logs.sql
-- Operation Type    : Schema Creation
-- Purpose           : create ota audit logs
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
-- V304: OTA Audit Logs
CREATE TABLE IF NOT EXISTS platform_ota_audit_log (
    id                      BIGSERIAL PRIMARY KEY,
    campaign_id             BIGINT NOT NULL,
    operator                VARCHAR(100) NOT NULL,
    action_type             VARCHAR(100) NOT NULL, -- CREATE_CAMPAIGN, START_CAMPAIGN, CANCEL_CAMPAIGN, FORCE_ROLLBACK
    previous_config         TEXT,
    new_config              TEXT,
    approval_id             VARCHAR(100),
    trace_id                VARCHAR(100) NOT NULL,
    reason                  VARCHAR(500),
    audited_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
