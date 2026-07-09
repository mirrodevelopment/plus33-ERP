-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 314
-- File              : V314__create_device_config_profiles.sql
-- Operation Type    : Schema Creation
-- Purpose           : create device config profiles
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
-- V314: Device Config Profiles
CREATE TABLE IF NOT EXISTS platform_device_config_profile (
    id                      BIGSERIAL PRIMARY KEY,
    version                 INT NOT NULL DEFAULT 0,
    profile_code            VARCHAR(100) NOT NULL UNIQUE,
    profile_name            VARCHAR(200) NOT NULL,
    profile_version         VARCHAR(50) NOT NULL,
    checksum                VARCHAR(64) NOT NULL,
    configuration_json      TEXT NOT NULL,
    rollback_profile_id     BIGINT,
    effective_from          TIMESTAMP,
    effective_to            TIMESTAMP,
    assignment_scope        VARCHAR(200) NOT NULL -- Base Profile, Warehouse Profile, POS Profile, Edge Gateway Profile
);
