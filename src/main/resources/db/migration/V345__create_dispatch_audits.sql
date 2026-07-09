-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 345
-- File              : V345__create_dispatch_audits.sql
-- Operation Type    : Schema Creation
-- Purpose           : create dispatch audits
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
-- V345: Dispatch Audits Logs
CREATE TABLE IF NOT EXISTS platform_dispatch_audit_log (
    id                          BIGSERIAL PRIMARY KEY,
    optimizer_version           VARCHAR(50) NOT NULL,
    planning_time_ms            BIGINT NOT NULL,
    decision_trace              TEXT NOT NULL,
    rollback_reference          VARCHAR(100),
    execution_id                VARCHAR(100) NOT NULL,
    audited_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
