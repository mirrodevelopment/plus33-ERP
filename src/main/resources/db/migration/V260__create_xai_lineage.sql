-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 260
-- File              : V260__create_xai_lineage.sql
-- Operation Type    : Schema Creation
-- Purpose           : create xai lineage
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
-- V260: Explainable Decision Lineage
CREATE TABLE IF NOT EXISTS platform_xai_lineage (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    decision_key        VARCHAR(150) NOT NULL UNIQUE,
    contributing_factors TEXT NOT NULL,
    model_version       VARCHAR(50) NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
