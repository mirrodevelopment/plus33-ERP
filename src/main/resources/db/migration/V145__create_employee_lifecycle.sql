-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 145
-- File              : V145__create_employee_lifecycle.sql
-- Operation Type    : Schema Creation
-- Purpose           : create employee lifecycle
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
-- V145: Employee Lifecycle and Document Expirations
CREATE TABLE IF NOT EXISTS hcm_employee_lifecycle (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL UNIQUE,
    lifecycle_status VARCHAR(30) NOT NULL DEFAULT 'CANDIDATE',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS hcm_employee_documents (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    document_type VARCHAR(50) NOT NULL, -- PASSPORT, VISA, etc.
    document_number VARCHAR(50) NOT NULL,
    expiry_date DATE NOT NULL,
    notified BOOLEAN NOT NULL DEFAULT FALSE
);
