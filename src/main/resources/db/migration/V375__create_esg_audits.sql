-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 375
-- File              : V375__create_esg_audits.sql
-- Operation Type    : Schema Creation
-- Purpose           : create esg audits
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
-- V375: ESG Report Audits
CREATE TABLE IF NOT EXISTS platform_esg_audit_log (
    id                          BIGSERIAL PRIMARY KEY,
    report_version              VARCHAR(50) NOT NULL,
    report_hash                 VARCHAR(256) NOT NULL,
    generated_by                VARCHAR(100) NOT NULL,
    approved_by                 VARCHAR(100) NOT NULL,
    approval_date               TIMESTAMP NOT NULL,
    digital_signature           TEXT NOT NULL,
    trace_id                    VARCHAR(100) NOT NULL,
    audited_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
