-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 189
-- File              : V189__create_dashboard_sharing.sql
-- Operation Type    : Schema Creation
-- Purpose           : create dashboard sharing
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
-- V189: Dashboard Shares and Collaboration DDL
CREATE TABLE IF NOT EXISTS bi_dashboard_share (
    id                  BIGSERIAL PRIMARY KEY,
    dashboard_code      VARCHAR(100) NOT NULL,
    shared_by           VARCHAR(100) NOT NULL,
    recipient_user      VARCHAR(100) NOT NULL,
    can_edit            BOOLEAN NOT NULL DEFAULT FALSE,
    shared_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(dashboard_code, recipient_user)
);

CREATE TABLE IF NOT EXISTS bi_dashboard_subscription (
    id                  BIGSERIAL PRIMARY KEY,
    dashboard_code      VARCHAR(100) NOT NULL,
    subscriber_user     VARCHAR(100) NOT NULL,
    schedule_cron       VARCHAR(100) NOT NULL,
    export_format       VARCHAR(50) NOT NULL DEFAULT 'PDF',
    status              VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_triggered_at   TIMESTAMP
);