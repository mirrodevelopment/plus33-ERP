-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 188
-- File              : V188__create_workload_management.sql
-- Operation Type    : Schema Creation
-- Purpose           : create workload management
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
-- V188: Workload Management & Query Optimization DDL
CREATE TABLE IF NOT EXISTS bi_workload_queue (
    id                  BIGSERIAL PRIMARY KEY,
    query_id            VARCHAR(100) NOT NULL UNIQUE,
    user_id             VARCHAR(100) NOT NULL,
    pool_name           VARCHAR(50) NOT NULL DEFAULT 'DEFAULT',
    priority            INTEGER NOT NULL DEFAULT 5,
    query_text          TEXT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'QUEUED',
    submitted_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    started_at          TIMESTAMP,
    ended_at            TIMESTAMP
);