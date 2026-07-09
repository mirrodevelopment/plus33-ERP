-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 140
-- File              : V140__create_project_analytics_views.sql
-- Operation Type    : Schema Creation
-- Purpose           : create project analytics views
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
-- V140: Project Analytics Snapshots
CREATE TABLE IF NOT EXISTS project_analytics_snapshots (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    metric_name VARCHAR(50) NOT NULL,
    metric_value NUMERIC(12, 4) NOT NULL,
    recorded_date DATE NOT NULL
);
