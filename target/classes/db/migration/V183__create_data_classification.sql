-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 183
-- File              : V183__create_data_classification.sql
-- Operation Type    : Schema Creation
-- Purpose           : create data classification
--
-- Tables Created    : IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V183: Data Classification & Privacy Governance Schema
CREATE TABLE IF NOT EXISTS bi_governance_classification (
    id                  BIGSERIAL PRIMARY KEY,
    table_name          VARCHAR(100) NOT NULL,
    column_name         VARCHAR(100) NOT NULL,
    classification_level VARCHAR(50) NOT NULL,
    description         TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(table_name, column_name)
);

CREATE TABLE IF NOT EXISTS bi_governance_masking_rule (
    id                  BIGSERIAL PRIMARY KEY,
    rule_name           VARCHAR(100) NOT NULL UNIQUE,
    classification_level VARCHAR(50) NOT NULL,
    masking_type        VARCHAR(50) NOT NULL,
    masking_pattern     VARCHAR(100),
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);