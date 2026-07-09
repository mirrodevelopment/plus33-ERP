-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 365
-- File              : V365__create_ev_energy_audits.sql
-- Operation Type    : Schema Creation
-- Purpose           : create ev energy audits
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
-- V365: Energy Audits Logs
CREATE TABLE IF NOT EXISTS platform_ev_energy_audit_log (
    id                          BIGSERIAL PRIMARY KEY,
    optimization_algorithm      VARCHAR(100) NOT NULL,
    energy_before_kwh           NUMERIC(10,2) NOT NULL,
    energy_after_kwh            NUMERIC(10,2) NOT NULL,
    estimated_cost              NUMERIC(12,2) NOT NULL,
    estimated_savings           NUMERIC(12,2) NOT NULL,
    carbon_offset_kg            NUMERIC(10,2) NOT NULL,
    operator                    VARCHAR(100) NOT NULL,
    trace_id                    VARCHAR(100) NOT NULL,
    execution_duration_ms       BIGINT NOT NULL,
    audited_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
