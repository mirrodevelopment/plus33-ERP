-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 115
-- File              : V115__create_esm_spare_parts.sql
-- Operation Type    : Schema Creation
-- Purpose           : create esm spare parts
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
-- V115: Spare Parts Van Inventory and Consumption
CREATE TABLE IF NOT EXISTS esm_van_stocks (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    van_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity_on_hand NUMERIC(18, 6) NOT NULL DEFAULT 0,
    unit_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS esm_parts_reservations (
    id BIGSERIAL PRIMARY KEY,
    work_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity_reserved NUMERIC(18, 6) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'RESERVED'
);
