-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 242
-- File              : V242__create_digital_twin_definitions.sql
-- Operation Type    : Schema Creation
-- Purpose           : create digital twin definitions
--
-- Tables Created    : IF, IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V242: Digital twin definitions & instances
CREATE TABLE IF NOT EXISTS platform_twin_definition (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    definition_code     VARCHAR(100) NOT NULL UNIQUE,
    definition_name     VARCHAR(150) NOT NULL,
    description         TEXT
);

CREATE TABLE IF NOT EXISTS platform_twin_instance (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    definition_id       BIGINT NOT NULL,
    instance_code       VARCHAR(100) NOT NULL UNIQUE,
    status              VARCHAR(50) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS platform_twin_state (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    instance_id         BIGINT NOT NULL UNIQUE,
    current_state_json  TEXT NOT NULL,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
