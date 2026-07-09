-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 315
-- File              : V315__create_device_compliance_audits.sql
-- Operation Type    : Schema Creation
-- Purpose           : create device compliance audits
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
-- V315: Compliance Audits Logs
CREATE TABLE IF NOT EXISTS platform_compliance_audit_log (
    id                      BIGSERIAL PRIMARY KEY,
    device_id               BIGINT NOT NULL,
    operator                VARCHAR(100) NOT NULL,
    action_type             VARCHAR(100) NOT NULL, -- UPDATE_POLICY, REMEDIATION, OVERRIDE
    previous_state          TEXT,
    new_state               TEXT,
    approval_id             VARCHAR(100),
    trace_id                VARCHAR(100) NOT NULL,
    reason                  VARCHAR(500),
    ip_address              VARCHAR(45),
    audited_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
