-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 284
-- File              : V284__create_geofence_settings_audits.sql
-- Operation Type    : Schema Creation
-- Purpose           : create geofence settings audits
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
-- V284: Geofence Settings Audits
CREATE TABLE IF NOT EXISTS platform_geofence_audit_log (
    id                  BIGSERIAL PRIMARY KEY,
    geofence_id         BIGINT NOT NULL,
    operator            VARCHAR(100) NOT NULL,
    action_type         VARCHAR(100) NOT NULL, -- CREATE, UPDATE, DELETE
    previous_geometry_wkt TEXT,
    new_geometry_wkt     TEXT,
    approval_id         VARCHAR(100),
    trace_id            VARCHAR(100),
    audited_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
