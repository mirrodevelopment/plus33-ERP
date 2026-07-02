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
