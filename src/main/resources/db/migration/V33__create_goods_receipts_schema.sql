-- ============================================================
-- V33__create_goods_receipts_schema.sql
-- PLUS33 ERP — Goods Receipt Management Tables & Trigger logic
-- ============================================================

-- Create goods_receipts table
CREATE TABLE goods_receipts (
    id BIGSERIAL PRIMARY KEY,
    receipt_number VARCHAR(50) NOT NULL UNIQUE,
    purchase_order_id BIGINT NOT NULL,
    company_id BIGINT NOT NULL,
    warehouse_id BIGINT,
    store_id BIGINT,
    received_by BIGINT NOT NULL,
    received_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(30) NOT NULL DEFAULT 'COMPLETED',
    remarks VARCHAR(255),
    supplier_delivery_note VARCHAR(100),
    supplier_invoice_number VARCHAR(100),
    client_reference_id UUID NOT NULL UNIQUE,
    total_quantity DECIMAL(14,2) NOT NULL DEFAULT 0,
    total_amount DECIMAL(14,2) NOT NULL DEFAULT 0,
    cancelled_by BIGINT,
    cancelled_at TIMESTAMP,
    cancellation_reason TEXT,

    CONSTRAINT fk_goods_receipts_po FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders(id),
    CONSTRAINT fk_goods_receipts_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_goods_receipts_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_goods_receipts_store FOREIGN KEY (store_id) REFERENCES stores(id),
    CONSTRAINT fk_goods_receipts_received_by FOREIGN KEY (received_by) REFERENCES users(id),
    CONSTRAINT fk_goods_receipts_cancelled_by FOREIGN KEY (cancelled_by) REFERENCES users(id),

    CONSTRAINT chk_goods_receipts_status CHECK (status IN ('COMPLETED', 'CANCELLED')),
    CONSTRAINT chk_goods_receipts_location CHECK (
        (warehouse_id IS NOT NULL AND store_id IS NULL)
        OR
        (warehouse_id IS NULL AND store_id IS NOT NULL)
    ),
    CONSTRAINT chk_goods_receipts_cancel_reason CHECK (
        status <> 'CANCELLED' OR cancellation_reason IS NOT NULL
    )
);

-- Create goods_receipt_items table
CREATE TABLE goods_receipt_items (
    id BIGSERIAL PRIMARY KEY,
    goods_receipt_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    received_quantity DECIMAL(12,2) NOT NULL,
    remarks VARCHAR(255),

    CONSTRAINT fk_gr_items_receipt FOREIGN KEY (goods_receipt_id) REFERENCES goods_receipts(id) ON DELETE CASCADE,
    CONSTRAINT fk_gr_items_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT chk_gr_items_quantity CHECK (received_quantity > 0),
    CONSTRAINT uk_gr_item_product UNIQUE (goods_receipt_id, product_id)
);

-- Create sequence for goods receipt number generation
CREATE SEQUENCE IF NOT EXISTS goods_receipt_seq START WITH 1 INCREMENT BY 1;

-- Alter stock_movements to add tracing fields
ALTER TABLE stock_movements ADD COLUMN reference_type VARCHAR(50);
ALTER TABLE stock_movements ADD COLUMN reference_id BIGINT;
ALTER TABLE stock_movements ADD COLUMN reference_number VARCHAR(50);

-- Alter purchase_orders, purchase_order_items, and inventory_stock to support optimistic locking
ALTER TABLE purchase_orders ADD COLUMN version BIGINT DEFAULT 0 NOT NULL;
ALTER TABLE purchase_order_items ADD COLUMN version BIGINT DEFAULT 0 NOT NULL;
ALTER TABLE inventory_stock ADD COLUMN version BIGINT DEFAULT 0 NOT NULL;

-- Trigger logic for database-level company validation
CREATE OR REPLACE FUNCTION validate_goods_receipt_company()
RETURNS TRIGGER AS $$
DECLARE
    po_company_id BIGINT;
    dest_company_id BIGINT;
BEGIN
    -- Validate PO company matches
    SELECT company_id INTO po_company_id FROM purchase_orders WHERE id = NEW.purchase_order_id;
    IF po_company_id <> NEW.company_id THEN
        RAISE EXCEPTION 'Goods receipt company does not match purchase order company';
    END IF;

    -- Validate destination company matches
    IF NEW.warehouse_id IS NOT NULL THEN
        SELECT r.company_id INTO dest_company_id 
        FROM warehouses w
        JOIN regions r ON w.region_id = r.id
        WHERE w.id = NEW.warehouse_id;
        
        IF dest_company_id <> NEW.company_id THEN
            RAISE EXCEPTION 'Destination warehouse does not belong to the same company';
        END IF;
    ELSIF NEW.store_id IS NOT NULL THEN
        SELECT r.company_id INTO dest_company_id 
        FROM stores s
        JOIN regions r ON s.region_id = r.id
        WHERE s.id = NEW.store_id;
        
        IF dest_company_id <> NEW.company_id THEN
            RAISE EXCEPTION 'Destination store does not belong to the same company';
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_goods_receipt_company
BEFORE INSERT OR UPDATE ON goods_receipts
FOR EACH ROW
EXECUTE FUNCTION validate_goods_receipt_company();

-- Performance Indexes
CREATE INDEX idx_gr_company_status ON goods_receipts(company_id, status);
CREATE INDEX idx_gr_po ON goods_receipts(purchase_order_id);
CREATE INDEX idx_gr_received_at ON goods_receipts(received_at);
CREATE INDEX idx_gr_warehouse ON goods_receipts(warehouse_id);
CREATE INDEX idx_gr_store ON goods_receipts(store_id);
CREATE INDEX idx_gr_client_reference ON goods_receipts(client_reference_id);
CREATE INDEX idx_gr_status_received_at ON goods_receipts(status, received_at);
CREATE INDEX idx_stock_movements_reference ON stock_movements(reference_type, reference_id);
