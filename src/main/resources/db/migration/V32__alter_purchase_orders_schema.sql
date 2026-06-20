-- ============================================================
-- V32__alter_purchase_orders_schema.sql
-- PLUS33 ERP — Purchase Orders schema changes
-- ============================================================

-- Alter purchase_orders table
ALTER TABLE purchase_orders ADD COLUMN company_id BIGINT;
ALTER TABLE purchase_orders ADD COLUMN notes TEXT;
ALTER TABLE purchase_orders ADD COLUMN issued_by BIGINT;
ALTER TABLE purchase_orders ADD COLUMN issued_at TIMESTAMP;
ALTER TABLE purchase_orders ADD COLUMN cancelled_by BIGINT;
ALTER TABLE purchase_orders ADD COLUMN cancelled_at TIMESTAMP;
ALTER TABLE purchase_orders ADD COLUMN cancellation_reason TEXT;
ALTER TABLE purchase_orders ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- PO Receipt Tracking Timestamps
ALTER TABLE purchase_orders ADD COLUMN received_at TIMESTAMP;
ALTER TABLE purchase_orders ADD COLUMN closed_at TIMESTAMP;
ALTER TABLE purchase_orders ADD COLUMN received_percentage DECIMAL(5,2) DEFAULT 0;

-- Financial Tracking Fields
ALTER TABLE purchase_orders ADD COLUMN subtotal_amount DECIMAL(12,2) DEFAULT 0;
ALTER TABLE purchase_orders ADD COLUMN tax_amount DECIMAL(12,2) DEFAULT 0;
ALTER TABLE purchase_orders ADD COLUMN discount_amount DECIMAL(12,2) DEFAULT 0;
ALTER TABLE purchase_orders ADD COLUMN total_amount DECIMAL(12,2) DEFAULT 0;
ALTER TABLE purchase_orders ADD COLUMN currency_code VARCHAR(10) DEFAULT 'AED';

-- Backfill company_id to 1 (PLUS33_GLOBAL) for existing rows
UPDATE purchase_orders SET company_id = 1 WHERE company_id IS NULL;
ALTER TABLE purchase_orders ALTER COLUMN company_id SET NOT NULL;

-- Add constraints
ALTER TABLE purchase_orders ADD CONSTRAINT fk_purchase_orders_company FOREIGN KEY (company_id) REFERENCES companies(id);
ALTER TABLE purchase_orders ADD CONSTRAINT fk_purchase_orders_issued FOREIGN KEY (issued_by) REFERENCES users(id);
ALTER TABLE purchase_orders ADD CONSTRAINT fk_purchase_orders_cancelled FOREIGN KEY (cancelled_by) REFERENCES users(id);

ALTER TABLE purchase_orders ADD CONSTRAINT chk_purchase_orders_status
    CHECK (status IN ('DRAFT', 'ISSUED', 'PARTIALLY_RECEIVED', 'RECEIVED', 'CLOSED', 'CANCELLED'));

-- Enforce one PO per Purchase Request mapping
ALTER TABLE purchase_orders ADD CONSTRAINT uk_purchase_orders_pr UNIQUE (purchase_request_id);

-- Alter purchase_order_items table
ALTER TABLE purchase_order_items ADD COLUMN remarks VARCHAR(255);
ALTER TABLE purchase_order_items ADD COLUMN remaining_quantity DECIMAL(12,2);

-- Backfill remaining_quantity to ordered_quantity for existing rows
UPDATE purchase_order_items SET remaining_quantity = ordered_quantity WHERE remaining_quantity IS NULL;
ALTER TABLE purchase_order_items ALTER COLUMN remaining_quantity SET NOT NULL;

-- Add CHECK constraint on purchase_order_items
ALTER TABLE purchase_order_items ADD CONSTRAINT chk_po_items_received CHECK (received_quantity <= ordered_quantity);

-- Create Optimized Indexes
CREATE INDEX IF NOT EXISTS idx_po_company_status ON purchase_orders(company_id, status);
CREATE INDEX IF NOT EXISTS idx_po_supplier ON purchase_orders(supplier_id);
CREATE INDEX IF NOT EXISTS idx_po_delivery_date ON purchase_orders(expected_delivery_date);
CREATE INDEX IF NOT EXISTS idx_po_purchase_request ON purchase_orders(purchase_request_id);
