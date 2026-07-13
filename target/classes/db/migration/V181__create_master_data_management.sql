-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 181
-- File              : V181__create_master_data_management.sql
-- Operation Type    : Schema Creation
-- Purpose           : create master data management
--
-- Tables Created    : IF, IF, IF, IF, IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V181: Master Data Management (MDM) DDL Schema
CREATE TABLE IF NOT EXISTS bi_mdm_golden_record (
    id                  BIGSERIAL PRIMARY KEY,
    record_type         VARCHAR(50) NOT NULL,
    display_name        VARCHAR(250) NOT NULL,
    email               VARCHAR(250),
    phone               VARCHAR(50),
    address             TEXT,
    tax_number          VARCHAR(50),
    status              VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bi_mdm_source_mapping (
    id                  BIGSERIAL PRIMARY KEY,
    golden_record_id    BIGINT NOT NULL REFERENCES bi_mdm_golden_record(id) ON DELETE CASCADE,
    source_system       VARCHAR(100) NOT NULL,
    source_table        VARCHAR(100) NOT NULL,
    source_dim_id       BIGINT NOT NULL,
    confidence_score    NUMERIC(5,2) NOT NULL DEFAULT 100.00,
    mapped_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(source_system, source_table, source_dim_id)
);

CREATE TABLE IF NOT EXISTS bi_mdm_duplicate_candidate (
    id                  BIGSERIAL PRIMARY KEY,
    record_type         VARCHAR(50) NOT NULL,
    source_system_a     VARCHAR(100) NOT NULL,
    source_table_a      VARCHAR(100) NOT NULL,
    source_dim_id_a     BIGINT NOT NULL,
    source_system_b     VARCHAR(100) NOT NULL,
    source_table_b      VARCHAR(100) NOT NULL,
    source_dim_id_b     BIGINT NOT NULL,
    similarity_score    NUMERIC(5,2) NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    detected_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at         TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bi_mdm_merge_request (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    request_code        VARCHAR(100) NOT NULL UNIQUE,
    record_type         VARCHAR(50) NOT NULL,
    source_system_a     VARCHAR(100) NOT NULL,
    source_dim_id_a     BIGINT NOT NULL,
    source_system_b     VARCHAR(100) NOT NULL,
    source_dim_id_b     BIGINT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'REQUESTED',
    requested_by        VARCHAR(100) NOT NULL,
    comments            TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bi_mdm_steward_assignment (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    merge_request_id    BIGINT NOT NULL REFERENCES bi_mdm_merge_request(id) ON DELETE CASCADE,
    steward_user        VARCHAR(100) NOT NULL,
    assigned_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_at              TIMESTAMP,
    status              VARCHAR(50) NOT NULL DEFAULT 'ASSIGNED'
);

CREATE TABLE IF NOT EXISTS bi_mdm_steward_decision (
    id                  BIGSERIAL PRIMARY KEY,
    steward_assignment_id BIGINT NOT NULL REFERENCES bi_mdm_steward_assignment(id) ON DELETE CASCADE,
    decision            VARCHAR(50) NOT NULL,
    notes               TEXT,
    decided_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);