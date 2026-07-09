-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 313
-- File              : V313__create_device_attestations.sql
-- Operation Type    : Schema Creation
-- Purpose           : create device attestations
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
-- V313: Device Attestations (TPM Quotes)
CREATE TABLE IF NOT EXISTS platform_device_attestation (
    id                      BIGSERIAL PRIMARY KEY,
    device_id               BIGINT NOT NULL,
    pcr_values              TEXT NOT NULL,
    ak_certificate          TEXT NOT NULL,
    ek_certificate          TEXT,
    nonce                   VARCHAR(128) NOT NULL,
    measured_boot_hash      VARCHAR(64),
    firmware_measurements   TEXT,
    secure_boot_status      BOOLEAN NOT NULL DEFAULT TRUE,
    tpm_manufacturer        VARCHAR(100),
    tpm_version             VARCHAR(50),
    attestation_result      VARCHAR(50) NOT NULL, -- VERIFIED, FAILED, SUSPECT
    trust_score             NUMERIC(5,2) NOT NULL,
    verified_by             VARCHAR(100) NOT NULL,
    verification_time       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    certificate_chain       TEXT
);
