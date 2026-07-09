-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 202
-- File              : V202__create_feature_flags.sql
-- Operation Type    : Schema Creation
-- Purpose           : create feature flags
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
-- V202: Feature Flags DDL
CREATE TABLE IF NOT EXISTS platform_feature_flag (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    flag_key            VARCHAR(250) NOT NULL UNIQUE,
    status              VARCHAR(50) NOT NULL DEFAULT 'DISABLED',
    rollout_percentage  INT NOT NULL DEFAULT 0,
    rules_json          TEXT,
    description         TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_feature_flag_history (
    id                  BIGSERIAL PRIMARY KEY,
    flag_key            VARCHAR(250) NOT NULL,
    previous_value      VARCHAR(50),
    new_value           VARCHAR(50),
    operator            VARCHAR(100),
    reason              TEXT,
    rollout_percentage  INT NOT NULL DEFAULT 0,
    changed_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
