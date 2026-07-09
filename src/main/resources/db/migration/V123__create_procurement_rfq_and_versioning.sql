-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 123
-- File              : V123__create_procurement_rfq_and_versioning.sql
-- Operation Type    : Schema Creation
-- Purpose           : create procurement rfq and versioning
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
-- V123: RFQ and RFQ Versioning
CREATE TABLE IF NOT EXISTS procurement_rfqs (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    rfq_number VARCHAR(50) NOT NULL UNIQUE,
    current_version INT NOT NULL DEFAULT 1,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS procurement_rfq_versions (
    id BIGSERIAL PRIMARY KEY,
    rfq_id BIGINT NOT NULL,
    version_number INT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    effective_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(rfq_id, version_number)
);

CREATE TABLE IF NOT EXISTS procurement_supplier_responses (
    id BIGSERIAL PRIMARY KEY,
    rfq_version_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    bid_amount NUMERIC(18, 2) NOT NULL,
    received_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
