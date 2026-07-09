-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 182
-- File              : V182__create_data_catalog.sql
-- Operation Type    : Schema Creation
-- Purpose           : create data catalog
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
-- V182: Data Catalog Schema
CREATE TABLE IF NOT EXISTS bi_catalog_dataset (
    id                  BIGSERIAL PRIMARY KEY,
    dataset_name        VARCHAR(100) NOT NULL UNIQUE,
    description         TEXT,
    owner_role          VARCHAR(100) NOT NULL,
    steward_user        VARCHAR(100),
    certification_status VARCHAR(50) NOT NULL DEFAULT 'BRONZE',
    last_certified_at   TIMESTAMP,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bi_catalog_glossary (
    id                  BIGSERIAL PRIMARY KEY,
    term_code           VARCHAR(100) NOT NULL UNIQUE,
    term_name           VARCHAR(150) NOT NULL,
    definition          TEXT NOT NULL,
    calculation_rule    TEXT,
    domain_area         VARCHAR(100) NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);