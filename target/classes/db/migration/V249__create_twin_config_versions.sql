-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 249
-- File              : V249__create_twin_config_versions.sql
-- Operation Type    : Schema Creation
-- Purpose           : create twin config versions
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
-- V249: Twin configuration versions
CREATE TABLE IF NOT EXISTS platform_twin_config_version (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    instance_id         BIGINT NOT NULL,
    config_version      VARCHAR(50) NOT NULL,
    configuration_json  TEXT NOT NULL,
    effective_from      TIMESTAMP NOT NULL,
    effective_to        TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_twin_config_history (
    id                  BIGSERIAL PRIMARY KEY,
    instance_id         BIGINT NOT NULL,
    previous_version    VARCHAR(50),
    new_version         VARCHAR(50) NOT NULL,
    operator            VARCHAR(100) NOT NULL,
    reason              TEXT,
    changed_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
