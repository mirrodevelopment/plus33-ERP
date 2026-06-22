-- ============================================================
-- V42__create_inventory_adjustments.sql
-- PLUS33 ERP — Inventory Adjustment schema and triggers
-- ============================================================

-- 1. Create sequence for inventory adjustment numbers
CREATE SEQUENCE IF NOT EXISTS inventory_adjustment_seq START WITH 1 INCREMENT BY 1;

-- 2. Create inventory_adjustments table
CREATE TABLE inventory_adjustments (
    id BIGSERIAL PRIMARY KEY,
    adjustment_number VARCHAR(50) NOT NULL UNIQUE,
    company_id BIGINT NOT NULL,
    warehouse_id BIGINT,
    store_id BIGINT,
    adjustment_type VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    client_reference_id UUID NOT NULL UNIQUE,
    remarks VARCHAR(255),
    created_by BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    submitted_by BIGINT,
    submitted_at TIMESTAMP,
    approved_by BIGINT,
    approved_at TIMESTAMP,
    posted_by BIGINT,
    posted_at TIMESTAMP,
    cancelled_by BIGINT,
    cancelled_at TIMESTAMP,
    cancellation_reason VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_adjustments_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_adjustments_wh FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_adjustments_store FOREIGN KEY (store_id) REFERENCES stores(id),
    CONSTRAINT fk_adjustments_created FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT fk_adjustments_submitted FOREIGN KEY (submitted_by) REFERENCES users(id),
    CONSTRAINT fk_adjustments_approved FOREIGN KEY (approved_by) REFERENCES users(id),
    CONSTRAINT fk_adjustments_posted FOREIGN KEY (posted_by) REFERENCES users(id),
    CONSTRAINT fk_adjustments_cancelled FOREIGN KEY (cancelled_by) REFERENCES users(id),

    CONSTRAINT chk_adjustments_status CHECK (
        status IN ('DRAFT', 'SUBMITTED', 'APPROVED', 'POSTED', 'CANCELLED')
    ),
    CONSTRAINT chk_adjustments_type CHECK (
        adjustment_type IN ('DAMAGE', 'EXPIRED', 'SHRINKAGE', 'FOUND_STOCK', 'STOCK_COUNT_VARIANCE', 'MANUAL_CORRECTION')
    ),
    -- XOR constraint for location: must reference exactly one location
    CONSTRAINT chk_adjustments_location CHECK (
        (warehouse_id IS NOT NULL AND store_id IS NULL)
        OR
        (warehouse_id IS NULL AND store_id IS NOT NULL)
    )
);

-- 3. Create inventory_adjustment_items table
CREATE TABLE inventory_adjustment_items (
    id BIGSERIAL PRIMARY KEY,
    adjustment_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity DECIMAL(12,2) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_adj_items_adjustment FOREIGN KEY (adjustment_id) REFERENCES inventory_adjustments(id) ON DELETE CASCADE,
    CONSTRAINT fk_adj_items_product FOREIGN KEY (product_id) REFERENCES products(id),

    CONSTRAINT chk_adj_items_qty_nonzero CHECK (quantity <> 0.00),
    CONSTRAINT uq_adjustment_items UNIQUE (adjustment_id, product_id) DEFERRABLE INITIALLY DEFERRED
);

-- 4. Trigger for database-level validation
CREATE OR REPLACE FUNCTION validate_inventory_adjustment()
RETURNS TRIGGER AS $$
DECLARE
    loc_company_id BIGINT;
BEGIN
    -- Get company ID of the referenced location
    IF NEW.warehouse_id IS NOT NULL THEN
        SELECT r.company_id INTO loc_company_id
        FROM warehouses w JOIN regions r ON w.region_id = r.id
        WHERE w.id = NEW.warehouse_id;
    ELSE
        SELECT r.company_id INTO loc_company_id
        FROM stores s JOIN regions r ON s.region_id = r.id
        WHERE s.id = NEW.store_id;
    END IF;

    IF loc_company_id IS NULL THEN
        RAISE EXCEPTION 'Invalid location reference';
    END IF;

    IF loc_company_id <> NEW.company_id THEN
        RAISE EXCEPTION 'Referenced location must belong to the adjustment company';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_validate_inventory_adjustment
BEFORE INSERT OR UPDATE ON inventory_adjustments
FOR EACH ROW
EXECUTE FUNCTION validate_inventory_adjustment();

-- 5. Create indexes for performance optimization
CREATE INDEX idx_adjustments_company ON inventory_adjustments(company_id);
CREATE INDEX idx_adjustments_status ON inventory_adjustments(status);
CREATE INDEX idx_adjustments_wh ON inventory_adjustments(warehouse_id);
CREATE INDEX idx_adjustments_store ON inventory_adjustments(store_id);
CREATE INDEX idx_adjustments_client_ref ON inventory_adjustments(client_reference_id);
CREATE INDEX idx_adjustment_items_adj ON inventory_adjustment_items(adjustment_id);
CREATE INDEX idx_adjustment_items_product ON inventory_adjustment_items(product_id);
