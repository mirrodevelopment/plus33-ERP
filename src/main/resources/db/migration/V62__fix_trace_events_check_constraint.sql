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
