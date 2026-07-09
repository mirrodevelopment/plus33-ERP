-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 191
-- File              : V191__create_event_mesh.sql
-- Operation Type    : Schema Creation
-- Purpose           : create event mesh
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
-- V191: Event Mesh schema DDL
CREATE TABLE IF NOT EXISTS bi_event_mesh_registry (
    id                  BIGSERIAL PRIMARY KEY,
    topic               VARCHAR(250) NOT NULL UNIQUE,
    publisher_module    VARCHAR(100) NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bi_event_mesh_dead_letter (
    id                  BIGSERIAL PRIMARY KEY,
    event_id            VARCHAR(100) NOT NULL,
    topic               VARCHAR(250) NOT NULL,
    payload             TEXT NOT NULL,
    exception_message   TEXT,
    retry_count         INT NOT NULL DEFAULT 0,
    status              VARCHAR(50) NOT NULL DEFAULT 'PENDING_REPLAY',
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
