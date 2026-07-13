-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 294
-- File              : V294__create_edge_audit_logs.sql
-- Operation Type    : Schema Creation
-- Purpose           : create edge audit logs
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
-- V294: Edge Audit Logs
CREATE TABLE IF NOT EXISTS platform_edge_audit_log (
    id                  BIGSERIAL PRIMARY KEY,
    node_id             BIGINT NOT NULL,
    operator            VARCHAR(100) NOT NULL,
    action_type         VARCHAR(100) NOT NULL, -- UPDATE_CONFIG, FIRMWARE_UPGRADE, DECOMMISSION
    previous_config     TEXT,
    new_config          TEXT,
    approval_id         VARCHAR(100),
    trace_id            VARCHAR(100) NOT NULL,
    reason              VARCHAR(500),
    audited_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
