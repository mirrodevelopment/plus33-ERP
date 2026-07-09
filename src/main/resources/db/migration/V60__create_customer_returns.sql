-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 60
-- File              : V60__create_customer_returns.sql
-- Operation Type    : Schema Creation
-- Purpose           : create customer returns
--
-- Tables Created    : customer_returns, customer_return_items, credit_notes, credit_note_items
-- Tables Altered    : customer_invoice_items, customer_invoices, inventory_trace_events, inventory_trace_events
-- Seed Data For     : N/A
-- Indexes           : IF, IF, IF, IF, IF, IF
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V60__create_customer_returns.sql
-- PLUS33 ERP — Customer Returns & Credit Notes Schema
-- ============================================================

-- 1. Create Sequences
CREATE SEQUENCE IF NOT EXISTS customer_return_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS credit_note_seq START WITH 1 INCREMENT BY 1;

-- 2. Create customer_returns Table
CREATE TABLE customer_returns (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    sales_order_id BIGINT,
    customer_invoice_id BIGINT,
    warehouse_id BIGINT,
    store_id BIGINT,
    return_number VARCHAR(50) NOT NULL,
    client_reference_id UUID NOT NULL,
    status VARCHAR(30) NOT NULL,
    reason VARCHAR(50) NOT NULL,
    remarks TEXT,
    created_by BIGINT NOT NULL,
    approved_by BIGINT,
    received_by BIGINT,
    inspected_by BIGINT,
    closed_by BIGINT,
    cancelled_by BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    approved_at TIMESTAMP,
    received_at TIMESTAMP,
    inspected_at TIMESTAMP,
    closed_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    cancellation_reason TEXT,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_customer_returns_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_customer_returns_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_customer_returns_sales_order FOREIGN KEY (sales_order_id) REFERENCES sales_orders(id),
    CONSTRAINT fk_customer_returns_invoice FOREIGN KEY (customer_invoice_id) REFERENCES customer_invoices(id),
    CONSTRAINT fk_customer_returns_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_customer_returns_store FOREIGN KEY (store_id) REFERENCES stores(id),
    CONSTRAINT fk_customer_returns_created_by FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT fk_customer_returns_approved_by FOREIGN KEY (approved_by) REFERENCES users(id),
    CONSTRAINT fk_customer_returns_received_by FOREIGN KEY (received_by) REFERENCES users(id),
    CONSTRAINT fk_customer_returns_inspected_by FOREIGN KEY (inspected_by) REFERENCES users(id),
    CONSTRAINT fk_customer_returns_closed_by FOREIGN KEY (closed_by) REFERENCES users(id),
    CONSTRAINT fk_customer_returns_cancelled_by FOREIGN KEY (cancelled_by) REFERENCES users(id),
    CONSTRAINT uk_customer_returns_number UNIQUE (company_id, return_number),
    CONSTRAINT uk_customer_returns_client_ref UNIQUE (company_id, client_reference_id),
    CONSTRAINT chk_customer_returns_location CHECK ((warehouse_id IS NOT NULL AND store_id IS NULL) OR (warehouse_id IS NULL AND store_id IS NOT NULL))
);

-- 3. Create customer_return_items Table
CREATE TABLE customer_return_items (
    id BIGSERIAL PRIMARY KEY,
    customer_return_id BIGINT NOT NULL,
    sales_order_item_id BIGINT,
    customer_invoice_item_id BIGINT,
    product_id BIGINT NOT NULL,
    quantity DECIMAL(14,4) NOT NULL,
    inspection_result VARCHAR(30),
    inspection_notes TEXT,
    lot_id BIGINT,
    serial_id BIGINT,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_return_items_return FOREIGN KEY (customer_return_id) REFERENCES customer_returns(id),
    CONSTRAINT fk_return_items_so_item FOREIGN KEY (sales_order_item_id) REFERENCES sales_order_items(id),
    CONSTRAINT fk_return_items_invoice_item FOREIGN KEY (customer_invoice_item_id) REFERENCES customer_invoice_items(id),
    CONSTRAINT fk_return_items_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_return_items_lot FOREIGN KEY (lot_id) REFERENCES inventory_lots(id),
    CONSTRAINT fk_return_items_serial FOREIGN KEY (serial_id) REFERENCES inventory_serials(id),
    CONSTRAINT uq_customer_return_item_product UNIQUE (customer_return_id, product_id)
);

-- 4. Create credit_notes Table
CREATE TABLE credit_notes (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    customer_return_id BIGINT,
    customer_invoice_id BIGINT,
    credit_note_number VARCHAR(50) NOT NULL,
    client_reference_id UUID NOT NULL,
    status VARCHAR(30) NOT NULL,
    subtotal_amount DECIMAL(14,2) NOT NULL,
    tax_amount DECIMAL(14,2) NOT NULL,
    discount_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
    total_amount DECIMAL(14,2) NOT NULL,
    journal_entry_id BIGINT,
    remarks TEXT,
    created_by BIGINT NOT NULL,
    approved_by BIGINT,
    cancelled_by BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    approved_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    cancellation_reason TEXT,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_credit_notes_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_credit_notes_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_credit_notes_return FOREIGN KEY (customer_return_id) REFERENCES customer_returns(id),
    CONSTRAINT fk_credit_notes_invoice FOREIGN KEY (customer_invoice_id) REFERENCES customer_invoices(id),
    CONSTRAINT fk_credit_notes_journal FOREIGN KEY (journal_entry_id) REFERENCES journal_entries(id),
    CONSTRAINT fk_credit_notes_created_by FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT fk_credit_notes_approved_by FOREIGN KEY (approved_by) REFERENCES users(id),
    CONSTRAINT fk_credit_notes_cancelled_by FOREIGN KEY (cancelled_by) REFERENCES users(id),
    CONSTRAINT uk_credit_notes_number UNIQUE (company_id, credit_note_number),
    CONSTRAINT uk_credit_notes_client_ref UNIQUE (company_id, client_reference_id)
);

-- 5. Create credit_note_items Table
CREATE TABLE credit_note_items (
    id BIGSERIAL PRIMARY KEY,
    credit_note_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity DECIMAL(14,4) NOT NULL,
    unit_price DECIMAL(14,4) NOT NULL,
    discount_percentage DECIMAL(5,2) NOT NULL DEFAULT 0,
    tax_percentage DECIMAL(5,2) NOT NULL DEFAULT 0,
    net_amount DECIMAL(14,2) NOT NULL,
    tax_amount DECIMAL(14,2) NOT NULL,
    discount_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
    total_amount DECIMAL(14,2) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_credit_note_items_note FOREIGN KEY (credit_note_id) REFERENCES credit_notes(id),
    CONSTRAINT fk_credit_note_items_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- 6. Alter customer_invoice_items to support partial returns tracking
ALTER TABLE customer_invoice_items ADD COLUMN IF NOT EXISTS returned_quantity DECIMAL(14,4) DEFAULT 0 NOT NULL;

-- 7. Alter customer_invoices to support credit notes tracking
ALTER TABLE customer_invoices ADD COLUMN IF NOT EXISTS credited_amount DECIMAL(14,2) DEFAULT 0 NOT NULL;

-- 8. Reconfigure inventory_trace_events check constraint to support CUSTOMER_RETURN reference type
ALTER TABLE inventory_trace_events DROP CONSTRAINT IF EXISTS chk_trace_reference;
ALTER TABLE inventory_trace_events ADD CONSTRAINT chk_trace_reference
    CHECK (reference_type IN ('GOODS_RECEIPT', 'INVENTORY_TRANSFER', 'INVENTORY_ADJUSTMENT', 'STOCK_COUNT', 'SALES_ORDER', 'INVENTORY_RECALL', 'CUSTOMER_RETURN'));

-- 9. Create Performance Indexes
CREATE INDEX IF NOT EXISTS idx_customer_returns_company ON customer_returns(company_id);
CREATE INDEX IF NOT EXISTS idx_customer_returns_customer ON customer_returns(customer_id);
CREATE INDEX IF NOT EXISTS idx_customer_returns_status ON customer_returns(status);
CREATE INDEX IF NOT EXISTS idx_credit_notes_company ON credit_notes(company_id);
CREATE INDEX IF NOT EXISTS idx_credit_notes_customer ON credit_notes(customer_id);
CREATE INDEX IF NOT EXISTS idx_credit_notes_status ON credit_notes(status);
