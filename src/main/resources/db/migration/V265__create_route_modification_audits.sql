-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 265
-- File              : V265__create_route_modification_audits.sql
-- Operation Type    : Schema Creation
-- Purpose           : create route modification audits
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
-- V265: Route Modification Audits
CREATE TABLE IF NOT EXISTS platform_route_audit_log (
    id                  BIGSERIAL PRIMARY KEY,
    transit_route_id    BIGINT NOT NULL,
    old_route_json      TEXT NOT NULL,
    new_route_json      TEXT NOT NULL,
    reason              TEXT NOT NULL,
    trigger_type        VARCHAR(100) NOT NULL, -- AUTONOMOUS, MANUAL
    operator            VARCHAR(100) NOT NULL,
    changed_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
