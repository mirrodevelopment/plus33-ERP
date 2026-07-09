-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 203
-- File              : V203__create_secrets.sql
-- Operation Type    : Schema Creation
-- Purpose           : create secrets
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
-- V203: Secrets DDL
CREATE TABLE IF NOT EXISTS platform_secret_definition (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    alias_path          VARCHAR(250) NOT NULL UNIQUE,
    secret_key          VARCHAR(250) NOT NULL,
    rotation_policy     VARCHAR(100),
    next_rotation       TIMESTAMP,
    last_rotation       TIMESTAMP,
    provider_version    VARCHAR(50) NOT NULL DEFAULT '1',
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
