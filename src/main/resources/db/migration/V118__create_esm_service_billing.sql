-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 118
-- File              : V118__create_esm_service_billing.sql
-- Operation Type    : Schema Creation
-- Purpose           : create esm service billing
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
-- V118: Service Billing State Machine
CREATE TABLE IF NOT EXISTS esm_billing_records (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    work_order_id BIGINT NOT NULL,
    billing_method VARCHAR(30) NOT NULL DEFAULT 'T_AND_M',
    amount NUMERIC(18, 2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'READY_TO_BILL',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
