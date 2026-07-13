-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 225
-- File              : V225__create_platform_policy_versioning.sql
-- Operation Type    : Schema Creation
-- Purpose           : create platform policy versioning
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
-- V225: Policy Versioning DDL
CREATE TABLE IF NOT EXISTS platform_policy_version (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    policy_id           BIGINT NOT NULL,
    policy_version      VARCHAR(50) NOT NULL,
    rego_content        TEXT NOT NULL,
    effective_from      TIMESTAMP NOT NULL,
    effective_to        TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_policy_history (
    id                  BIGSERIAL PRIMARY KEY,
    policy_code         VARCHAR(100) NOT NULL,
    previous_version    VARCHAR(50),
    new_version         VARCHAR(50) NOT NULL,
    operator            VARCHAR(100) NOT NULL,
    reason              TEXT,
    changed_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
