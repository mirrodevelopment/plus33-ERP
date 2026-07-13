-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 62
-- File              : V62__fix_trace_events_check_constraint.sql
-- Operation Type    : Schema Alteration
-- Purpose           : fix trace events check constraint
--
-- Tables Created    : N/A
-- Tables Altered    : inventory_trace_events, inventory_trace_events, inventory_trace_events
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V62__fix_trace_events_check_constraint.sql
-- PLUS33 ERP — Correct check constraint on inventory_trace_events
-- ============================================================

-- 1. Drop the original check constraint if it exists (which was named chk_trace_ref_type, not chk_trace_reference)
ALTER TABLE inventory_trace_events DROP CONSTRAINT IF EXISTS chk_trace_ref_type;

-- 2. Drop the redundant chk_trace_reference if it was created in V60
ALTER TABLE inventory_trace_events DROP CONSTRAINT IF EXISTS chk_trace_reference;

-- 3. Add the corrected check constraint with the original name chk_trace_ref_type
ALTER TABLE inventory_trace_events ADD CONSTRAINT chk_trace_ref_type
    CHECK (reference_type IN ('GOODS_RECEIPT', 'INVENTORY_TRANSFER', 'INVENTORY_ADJUSTMENT', 'STOCK_COUNT', 'SALES_ORDER', 'INVENTORY_RECALL', 'CUSTOMER_RETURN'));
