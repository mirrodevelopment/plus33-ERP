-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 201
-- File              : V201__create_platform_configuration.sql
-- Operation Type    : Schema Creation
-- Purpose           : create platform configuration
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
-- V201: Platform Configuration DDL
CREATE TABLE IF NOT EXISTS platform_config (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 1,
    config_key          VARCHAR(250) NOT NULL UNIQUE,
    config_value        TEXT NOT NULL,
    profile             VARCHAR(50) NOT NULL DEFAULT 'default',
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_config_version (
    id                  BIGSERIAL PRIMARY KEY,
    config_id           BIGINT NOT NULL,
    version             INT NOT NULL,
    previous_version    INT,
    config_value        TEXT NOT NULL,
    effective_from      TIMESTAMP NOT NULL,
    effective_to        TIMESTAMP,
    checksum            VARCHAR(100) NOT NULL,
    modified_by         VARCHAR(100) NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(config_id, version)
);
