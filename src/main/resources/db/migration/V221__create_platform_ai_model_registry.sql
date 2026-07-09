-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 221
-- File              : V221__create_platform_ai_model_registry.sql
-- Operation Type    : Schema Creation
-- Purpose           : create platform ai model registry
--
-- Tables Created    : IF, IF, IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V221: AI Model Registry DDL
CREATE TABLE IF NOT EXISTS platform_ai_model_registry (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    model_code          VARCHAR(100) NOT NULL UNIQUE,
    description         TEXT
);

CREATE TABLE IF NOT EXISTS platform_ai_model_version (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    model_id            BIGINT NOT NULL,
    model_version       VARCHAR(50) NOT NULL,
    active              BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_ai_model_metrics (
    id                  BIGSERIAL PRIMARY KEY,
    model_version_id    BIGINT NOT NULL,
    rmse                NUMERIC(10,4),
    mae                 NUMERIC(10,4),
    recorded_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_ai_prediction_explanation (
    id                  BIGSERIAL PRIMARY KEY,
    prediction_time     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    prediction_target   VARCHAR(150) NOT NULL,
    reasoning           TEXT NOT NULL,
    confidence          NUMERIC(5,2) NOT NULL
);
