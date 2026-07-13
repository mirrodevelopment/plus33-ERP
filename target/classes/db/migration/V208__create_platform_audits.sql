-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 208
-- File              : V208__create_platform_audits.sql
-- Operation Type    : Schema Creation
-- Purpose           : create platform audits
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
-- V208: Platform Audits DDL
CREATE TABLE IF NOT EXISTS platform_audit_log (
    id                  BIGSERIAL PRIMARY KEY,
    action_name         VARCHAR(100) NOT NULL,
    user_identity       VARCHAR(100) NOT NULL,
    trace_context       VARCHAR(250),
    payload_diff        TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
