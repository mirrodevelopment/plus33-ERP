-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 190
-- File              : V190__create_self_service_analytics.sql
-- Operation Type    : Schema Creation
-- Purpose           : create self service analytics
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
-- V190: Self-Service Personal Workspace and Custom Measures Schema
CREATE TABLE IF NOT EXISTS bi_self_service_workspace (
    id                  BIGSERIAL PRIMARY KEY,
    workspace_code      VARCHAR(100) NOT NULL UNIQUE,
    owner_user          VARCHAR(100) NOT NULL,
    company_id          BIGINT NOT NULL,
    workspace_name      VARCHAR(150) NOT NULL,
    config_json         TEXT NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);