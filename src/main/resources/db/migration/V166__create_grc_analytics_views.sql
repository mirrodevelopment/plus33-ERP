-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 166
-- File              : V166__create_grc_analytics_views.sql
-- Operation Type    : Schema Creation
-- Purpose           : create grc analytics views
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
-- V166: GRC Analytics Views
CREATE TABLE IF NOT EXISTS grc_analytics_snapshots (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    metric_name VARCHAR(80) NOT NULL,
    metric_value NUMERIC(12, 4) NOT NULL,
    recorded_date DATE NOT NULL
);
