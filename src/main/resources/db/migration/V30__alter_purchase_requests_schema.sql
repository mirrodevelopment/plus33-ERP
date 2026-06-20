-- ============================================================
-- V30__alter_purchase_requests_schema.sql
-- PLUS33 ERP — Purchase Requests schema changes
-- ============================================================

-- Create sequences for request and order number generation
CREATE SEQUENCE IF NOT EXISTS purchase_request_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS purchase_order_seq START WITH 1 INCREMENT BY 1;

-- Alter purchase_requests table
ALTER TABLE purchase_requests ADD COLUMN company_id BIGINT;
ALTER TABLE purchase_requests ADD COLUMN supplier_id BIGINT;
ALTER TABLE purchase_requests ADD COLUMN submitted_by BIGINT;
ALTER TABLE purchase_requests ADD COLUMN approved_by BIGINT;
ALTER TABLE purchase_requests ADD COLUMN request_date DATE;
ALTER TABLE purchase_requests ADD COLUMN required_date DATE;
ALTER TABLE purchase_requests ADD COLUMN notes TEXT;

-- Explicit Workflow Timestamps
ALTER TABLE purchase_requests ADD COLUMN submitted_at TIMESTAMP;
ALTER TABLE purchase_requests ADD COLUMN approved_at TIMESTAMP;
ALTER TABLE purchase_requests ADD COLUMN rejected_at TIMESTAMP;
ALTER TABLE purchase_requests ADD COLUMN cancelled_at TIMESTAMP;
ALTER TABLE purchase_requests ADD COLUMN converted_to_po_at TIMESTAMP;

-- Rejection and Cancellation Reasons
ALTER TABLE purchase_requests ADD COLUMN rejection_reason TEXT;
ALTER TABLE purchase_requests ADD COLUMN cancellation_reason TEXT;

-- Purchase Order Linkage
ALTER TABLE purchase_requests ADD COLUMN purchase_order_id BIGINT;

-- Audit Timestamps (renaming or adding created_at/updated_at)
ALTER TABLE purchase_requests ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE purchase_requests ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Backfill company_id to 1 (PLUS33_GLOBAL) for existing rows
UPDATE purchase_requests SET company_id = 1 WHERE company_id IS NULL;
ALTER TABLE purchase_requests ALTER COLUMN company_id SET NOT NULL;

-- Update status constraint: drop previous default, set new default, update existing rows if they are 'PENDING'
ALTER TABLE purchase_requests ALTER COLUMN status SET DEFAULT 'DRAFT';
UPDATE purchase_requests SET status = 'DRAFT' WHERE status = 'PENDING';

-- Add constraints
ALTER TABLE purchase_requests ADD CONSTRAINT fk_purchase_requests_company FOREIGN KEY (company_id) REFERENCES companies(id);
ALTER TABLE purchase_requests ADD CONSTRAINT fk_purchase_requests_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id);
ALTER TABLE purchase_requests ADD CONSTRAINT fk_purchase_requests_submitted FOREIGN KEY (submitted_by) REFERENCES users(id);
ALTER TABLE purchase_requests ADD CONSTRAINT fk_purchase_requests_approved FOREIGN KEY (approved_by) REFERENCES users(id);
ALTER TABLE purchase_requests ADD CONSTRAINT fk_purchase_requests_po FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders(id);

ALTER TABLE purchase_requests ADD CONSTRAINT chk_purchase_requests_status 
    CHECK (status IN ('DRAFT', 'SUBMITTED', 'APPROVED', 'REJECTED', 'CANCELLED', 'CONVERTED_TO_PO'));

-- Alter purchase_request_items table
ALTER TABLE purchase_request_items RENAME COLUMN quantity TO requested_quantity;
ALTER TABLE purchase_request_items ADD COLUMN approved_quantity DECIMAL(12,2);
ALTER TABLE purchase_request_items ADD COLUMN unit_of_measure VARCHAR(50);
ALTER TABLE purchase_request_items ADD COLUMN remarks VARCHAR(255);

-- Create Optimized Indexes
CREATE INDEX IF NOT EXISTS idx_pr_company_status ON purchase_requests(company_id, status);
CREATE INDEX IF NOT EXISTS idx_pr_supplier ON purchase_requests(supplier_id);
CREATE INDEX IF NOT EXISTS idx_pr_required_date ON purchase_requests(required_date);
CREATE INDEX IF NOT EXISTS idx_pr_request_date ON purchase_requests(request_date);
