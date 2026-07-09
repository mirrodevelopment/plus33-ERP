-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 122
-- File              : V122__create_procurement_requisitions.sql
-- Operation Type    : Schema Creation
-- Purpose           : create procurement requisitions
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
-- V122: Advanced Procurement Requisitions and Purchase Orders
CREATE TABLE IF NOT EXISTS procurement_requisitions (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    requisition_number VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL DEFAULT 'REQUISITION_DRAFT',
    total_amount NUMERIC(18, 2) NOT NULL DEFAULT 0,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
