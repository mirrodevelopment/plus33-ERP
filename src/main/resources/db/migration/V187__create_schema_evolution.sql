-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 187
-- File              : V187__create_schema_evolution.sql
-- Operation Type    : Schema Creation
-- Purpose           : create schema evolution
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
-- V187: Schema Drift & Evolution Tracking Schema
CREATE TABLE IF NOT EXISTS bi_schema_evolution_history (
    id                  BIGSERIAL PRIMARY KEY,
    table_name          VARCHAR(100) NOT NULL,
    detected_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    action_type         VARCHAR(50) NOT NULL,
    action_detail       TEXT NOT NULL,
    validation_status   VARCHAR(50) NOT NULL DEFAULT 'PENDING'
);