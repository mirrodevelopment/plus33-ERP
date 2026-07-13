-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 184
-- File              : V184__create_dashboard_usage.sql
-- Operation Type    : Schema Creation
-- Purpose           : create dashboard usage
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
-- V184: Dashboard Analytics and Usage Logging Schema
CREATE TABLE IF NOT EXISTS bi_usage_log (
    id                  BIGSERIAL PRIMARY KEY,
    user_id             VARCHAR(100) NOT NULL,
    dashboard_code      VARCHAR(100) NOT NULL,
    action_type         VARCHAR(50) NOT NULL,
    duration_ms         BIGINT NOT NULL,
    company_id          BIGINT NOT NULL,
    ip_address          VARCHAR(45),
    accessed_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);