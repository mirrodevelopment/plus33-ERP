-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 301
-- File              : V301__create_ota_packages.sql
-- Operation Type    : Schema Creation
-- Purpose           : create ota packages
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
-- V301: OTA Packages
CREATE TABLE IF NOT EXISTS platform_ota_package (
    id                      BIGSERIAL PRIMARY KEY,
    version                 INT NOT NULL DEFAULT 0,
    package_name            VARCHAR(200) NOT NULL,
    package_version         VARCHAR(50) NOT NULL,
    semantic_version        VARCHAR(50) NOT NULL,
    checksum_sha256         VARCHAR(64) NOT NULL,
    signature               VARCHAR(256) NOT NULL,
    package_size_bytes      BIGINT NOT NULL,
    compression             VARCHAR(50) NOT NULL DEFAULT 'GZIP',
    supported_architecture  VARCHAR(50) NOT NULL, -- x86_64, arm64
    supported_os            VARCHAR(50) NOT NULL, -- Linux, Windows
    minimum_agent_version   VARCHAR(50),
    rollback_version        VARCHAR(50),
    release_notes           TEXT,
    package_type            VARCHAR(50) NOT NULL -- FULL, DELTA, PATCH, HOTFIX
);
