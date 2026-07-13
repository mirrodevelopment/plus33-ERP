-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 224
-- File              : V224__create_platform_policy_engine.sql
-- Operation Type    : Schema Creation
-- Purpose           : create platform policy engine
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
-- V224: Policy Engine DDL
CREATE TABLE IF NOT EXISTS platform_access_policy (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    policy_code         VARCHAR(100) NOT NULL UNIQUE,
    rego_content        TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_policy_audit (
    id                  BIGSERIAL PRIMARY KEY,
    policy_code         VARCHAR(100) NOT NULL,
    user_identity       VARCHAR(100) NOT NULL,
    action              VARCHAR(150) NOT NULL,
    decision            VARCHAR(50) NOT NULL, -- ALLOW, DENY
    evaluated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
