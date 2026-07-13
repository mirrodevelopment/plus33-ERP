-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 159
-- File              : V159__create_enterprise_control_library.sql
-- Operation Type    : Schema Creation
-- Purpose           : create enterprise control library
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
-- V159: Enterprise Control Library
CREATE TABLE IF NOT EXISTS grc_control_library (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    control_code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    owner_id BIGINT
);

CREATE TABLE IF NOT EXISTS grc_control_mappings (
    id BIGSERIAL PRIMARY KEY,
    control_library_id BIGINT NOT NULL,
    framework_id BIGINT NOT NULL,
    control_ref VARCHAR(50) NOT NULL,
    UNIQUE(control_library_id, framework_id)
);
