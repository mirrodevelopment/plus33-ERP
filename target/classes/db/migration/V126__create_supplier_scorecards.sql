-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 126
-- File              : V126__create_supplier_scorecards.sql
-- Operation Type    : Schema Creation
-- Purpose           : create supplier scorecards
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
-- V126: Supplier Performance Scorecards
CREATE TABLE IF NOT EXISTS procurement_supplier_scorecards (
    id BIGSERIAL PRIMARY KEY,
    supplier_id BIGINT NOT NULL UNIQUE,
    on_time_delivery_rate NUMERIC(5, 2) NOT NULL DEFAULT 100.00,
    quality_defect_rate NUMERIC(5, 2) NOT NULL DEFAULT 0.00,
    invoice_accuracy_rate NUMERIC(5, 2) NOT NULL DEFAULT 100.00,
    overall_rating NUMERIC(5, 2) NOT NULL DEFAULT 100.00,
    recalculated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
