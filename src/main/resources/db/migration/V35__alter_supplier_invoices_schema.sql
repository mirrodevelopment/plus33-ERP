-- ============================================================
-- V35__alter_supplier_invoices_schema.sql
-- PLUS33 ERP — Supplier Invoice Schema Enhancements
-- ============================================================

-- 1. Drop check constraint on status on supplier_invoices if it exists
ALTER TABLE supplier_invoices DROP CONSTRAINT IF EXISTS chk_supplier_invoice_status;

-- 2. Alter supplier_invoices columns
ALTER TABLE supplier_invoices ALTER COLUMN status SET DEFAULT 'DRAFT';
ALTER TABLE supplier_invoices ADD COLUMN IF NOT EXISTS subtotal_amount DECIMAL(14,2) NOT NULL DEFAULT 0.00;
ALTER TABLE supplier_invoices ADD COLUMN IF NOT EXISTS tax_amount DECIMAL(14,2) NOT NULL DEFAULT 0.00;
ALTER TABLE supplier_invoices ADD COLUMN IF NOT EXISTS discount_amount DECIMAL(14,2) NOT NULL DEFAULT 0.00;
ALTER TABLE supplier_invoices ADD COLUMN IF NOT EXISTS paid_amount DECIMAL(14,2) NOT NULL DEFAULT 0.00;
ALTER TABLE supplier_invoices ADD COLUMN IF NOT EXISTS outstanding_balance DECIMAL(14,2) NOT NULL DEFAULT 0.00;
ALTER TABLE supplier_invoices ADD COLUMN IF NOT EXISTS journal_entry_id BIGINT;
ALTER TABLE supplier_invoices ADD COLUMN IF NOT EXISTS version BIGINT NOT NULL DEFAULT 0;

-- 3. Add constraint for new statuses and foreign key to journal entries
ALTER TABLE supplier_invoices ADD CONSTRAINT chk_supplier_invoice_status 
    CHECK (status IN ('DRAFT', 'APPROVED', 'PARTIALLY_PAID', 'PAID', 'CANCELLED'));

ALTER TABLE supplier_invoices ADD CONSTRAINT fk_supplier_invoice_journal
    FOREIGN KEY (journal_entry_id) REFERENCES journal_entries(id);

-- 4. Create table supplier_invoice_items
CREATE TABLE supplier_invoice_items (
    id BIGSERIAL PRIMARY KEY,
    supplier_invoice_id BIGINT NOT NULL,
    purchase_order_item_id BIGINT NOT NULL,
    goods_receipt_item_id BIGINT,
    product_id BIGINT NOT NULL,
    quantity DECIMAL(12,2) NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    net_amount DECIMAL(14,2) NOT NULL,
    tax_rate DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    tax_amount DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    discount_amount DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(14,2) NOT NULL,
    
    CONSTRAINT fk_invoice_items_invoice FOREIGN KEY (supplier_invoice_id) REFERENCES supplier_invoices(id) ON DELETE CASCADE,
    CONSTRAINT fk_invoice_items_po_item FOREIGN KEY (purchase_order_item_id) REFERENCES purchase_order_items(id),
    CONSTRAINT fk_invoice_items_gr_item FOREIGN KEY (goods_receipt_item_id) REFERENCES goods_receipt_items(id),
    CONSTRAINT fk_invoice_items_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT chk_invoice_items_quantity CHECK (quantity > 0),
    CONSTRAINT chk_invoice_items_unit_price CHECK (unit_price >= 0),
    CONSTRAINT uk_supplier_invoice_item_po_item UNIQUE (supplier_invoice_id, purchase_order_item_id)
);

-- 5. Add invoiced_quantity to purchase_order_items
ALTER TABLE purchase_order_items ADD COLUMN IF NOT EXISTS invoiced_quantity DECIMAL(12,2) NOT NULL DEFAULT 0.00;

-- 6. Validation trigger for supplier_invoices
CREATE OR REPLACE FUNCTION validate_supplier_invoice()
RETURNS TRIGGER AS $$
DECLARE
    po_company_id BIGINT;
    po_supplier_id BIGINT;
BEGIN
    IF NEW.purchase_order_id IS NOT NULL THEN
        -- Verify PO company matches invoice company
        SELECT company_id, supplier_id INTO po_company_id, po_supplier_id 
        FROM purchase_orders 
        WHERE id = NEW.purchase_order_id;
        
        IF po_company_id <> NEW.company_id THEN
            RAISE EXCEPTION 'Invoice company does not match purchase order company';
        END IF;
        
        -- Verify PO supplier matches invoice supplier
        IF po_supplier_id <> NEW.supplier_id THEN
            RAISE EXCEPTION 'Invoice supplier does not match purchase order supplier';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_validate_supplier_invoice
BEFORE INSERT OR UPDATE ON supplier_invoices
FOR EACH ROW
EXECUTE FUNCTION validate_supplier_invoice();

-- 7. Database sequence for journal entries and indexes
CREATE SEQUENCE IF NOT EXISTS journal_entry_seq START WITH 1 INCREMENT BY 1;

CREATE INDEX IF NOT EXISTS idx_supplier_invoice_status ON supplier_invoices(status);
CREATE INDEX IF NOT EXISTS idx_supplier_invoice_items_invoice ON supplier_invoice_items(supplier_invoice_id);
CREATE INDEX IF NOT EXISTS idx_supplier_invoice_items_po_item ON supplier_invoice_items(purchase_order_item_id);
