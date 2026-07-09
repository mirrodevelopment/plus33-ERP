-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 335
-- File              : V335__create_routing_audits.sql
-- Operation Type    : Schema Creation
-- Purpose           : create routing audits
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
-- V335: Routing Audits Logs
CREATE TABLE IF NOT EXISTS platform_routing_audit_log (
    id                      BIGSERIAL PRIMARY KEY,
    previous_route          TEXT,
    new_route               TEXT,
    reason                  VARCHAR(500),
    optimizer               VARCHAR(100) NOT NULL,
    approved_by             VARCHAR(100) NOT NULL,
    execution_status        VARCHAR(50) NOT NULL, -- COMPLETED, FAILED, ROLLBACK
    rollback_reference      VARCHAR(100),
    trace_id                VARCHAR(100) NOT NULL,
    audited_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
