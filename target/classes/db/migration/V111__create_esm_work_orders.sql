-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 111
-- File              : V111__create_esm_work_orders.sql
-- Operation Type    : Schema Creation
-- Purpose           : create esm work orders
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
-- V111: ESM Core Work Orders and Technician Assignments
CREATE TABLE IF NOT EXISTS esm_work_orders (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    case_id BIGINT,
    customer_id BIGINT NOT NULL,
    installed_asset_id BIGINT,
    work_order_number VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    technician_id BIGINT,
    scheduled_at TIMESTAMP,
    actual_duration_minutes INT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
