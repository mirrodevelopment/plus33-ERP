-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 295
-- File              : V295__create_edge_certificates.sql
-- Operation Type    : Schema Creation
-- Purpose           : create edge certificates
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
-- V295: Edge Certificate Rotation Logs
CREATE TABLE IF NOT EXISTS platform_edge_certificate_log (
    id                  BIGSERIAL PRIMARY KEY,
    node_id             BIGINT NOT NULL,
    certificate_serial  VARCHAR(100) NOT NULL,
    issuer              VARCHAR(200) NOT NULL,
    valid_from          TIMESTAMP NOT NULL,
    valid_to            TIMESTAMP NOT NULL,
    rotation_reason     VARCHAR(200),
    algorithm           VARCHAR(50) NOT NULL DEFAULT 'RSA',
    key_length          INT NOT NULL DEFAULT 2048,
    rotation_status     VARCHAR(50) NOT NULL, -- PENDING, COMPLETED, FAILED
    rotated_by          VARCHAR(100) NOT NULL,
    rotated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
