-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 97
-- File              : V97__create_wms_rules_and_workflow_tables.sql
-- Operation Type    : Schema Creation
-- Purpose           : create wms rules and workflow tables
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
-- V97: Rules Engine, Workflow Process Engine & Offline Queue Tables
CREATE TABLE IF NOT EXISTS warehouse_rules (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    rule_code VARCHAR(50) NOT NULL UNIQUE,
    rule_name VARCHAR(100) NOT NULL,
    rule_type VARCHAR(30) NOT NULL, -- HAZMAT, TEMPERATURE, WEIGHT, VELOCITY
    condition_expression TEXT NOT NULL,
    action_expression TEXT NOT NULL,
    priority INT DEFAULT 5,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS warehouse_workflows (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    workflow_code VARCHAR(50) NOT NULL UNIQUE,
    workflow_name VARCHAR(100) NOT NULL,
    process_type VARCHAR(30) NOT NULL, -- INBOUND, OUTBOUND, CROSS_DOCK
    steps_json TEXT NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS offline_event_queue (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    device_id VARCHAR(100) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    payload_json TEXT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    logged_at TIMESTAMP NOT NULL,
    synced_at TIMESTAMP,
    error_message TEXT
);
