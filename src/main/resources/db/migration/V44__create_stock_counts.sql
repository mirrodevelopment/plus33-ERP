-- ============================================================
-- V44__create_stock_counts.sql
-- PLUS33 ERP — Stock Count & Cycle Count schema, constraints, and triggers
-- ============================================================

-- 1. Create sequence for stock count session numbers
CREATE SEQUENCE IF NOT EXISTS stock_count_seq START WITH 1 INCREMENT BY 1;

-- 2. Create stock_counts table
CREATE TABLE stock_counts (
    id BIGSERIAL PRIMARY KEY,
    count_number VARCHAR(50) NOT NULL UNIQUE,
    company_id BIGINT NOT NULL,
    warehouse_id BIGINT,
    store_id BIGINT,
    status VARCHAR(30) NOT NULL,
    count_type VARCHAR(30) NOT NULL,
    blind_count BOOLEAN NOT NULL DEFAULT TRUE,
    assigned_to BIGINT,
    adjustment_id BIGINT,
    approval_required BOOLEAN NOT NULL DEFAULT FALSE,
    approval_threshold_percentage DECIMAL(5,2) NOT NULL DEFAULT 5.00,
    client_reference_id UUID NOT NULL UNIQUE,
    remarks VARCHAR(255),
    rejection_reason VARCHAR(255),
    recount_number INTEGER NOT NULL DEFAULT 0,
    created_by BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    assigned_by BIGINT,
    assigned_at TIMESTAMP,
    started_by BIGINT,
    started_at TIMESTAMP,
    submitted_by BIGINT,
    submitted_at TIMESTAMP,
    approved_by BIGINT,
    approved_at TIMESTAMP,
    posted_by BIGINT,
    posted_at TIMESTAMP,
    closed_by BIGINT,
    closed_at TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_stock_counts_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_stock_counts_wh FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_stock_counts_store FOREIGN KEY (store_id) REFERENCES stores(id),
    CONSTRAINT fk_stock_counts_assigned_to FOREIGN KEY (assigned_to) REFERENCES users(id),
    CONSTRAINT fk_stock_counts_adjustment FOREIGN KEY (adjustment_id) REFERENCES inventory_adjustments(id),
    CONSTRAINT fk_stock_counts_created FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT fk_stock_counts_assigned_by FOREIGN KEY (assigned_by) REFERENCES users(id),
    CONSTRAINT fk_stock_counts_started_by FOREIGN KEY (started_by) REFERENCES users(id),
    CONSTRAINT fk_stock_counts_submitted_by FOREIGN KEY (submitted_by) REFERENCES users(id),
    CONSTRAINT fk_stock_counts_approved_by FOREIGN KEY (approved_by) REFERENCES users(id),
    CONSTRAINT fk_stock_counts_posted_by FOREIGN KEY (posted_by) REFERENCES users(id),
    CONSTRAINT fk_stock_counts_closed_by FOREIGN KEY (closed_by) REFERENCES users(id),

    CONSTRAINT chk_stock_counts_status CHECK (
        status IN ('DRAFT', 'ASSIGNED', 'IN_PROGRESS', 'SUBMITTED', 'REJECTED', 'APPROVED', 'POSTED', 'CLOSED')
    ),
    CONSTRAINT chk_stock_counts_type CHECK (
        count_type IN ('FULL', 'CYCLE')
    ),
    CONSTRAINT chk_stock_counts_location CHECK (
        (warehouse_id IS NOT NULL AND store_id IS NULL)
        OR
        (warehouse_id IS NULL AND store_id IS NOT NULL)
    ),
    CONSTRAINT chk_approval_threshold_percentage CHECK (
        approval_threshold_percentage >= 0.00 AND approval_threshold_percentage <= 100.00
    )
);

-- 3. Create stock_count_items table
CREATE TABLE stock_count_items (
    id BIGSERIAL PRIMARY KEY,
    stock_count_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    system_quantity DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    reserved_quantity DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    available_quantity DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    counted_quantity DECIMAL(12,2),
    variance DECIMAL(12,2),
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_sc_items_count FOREIGN KEY (stock_count_id) REFERENCES stock_counts(id) ON DELETE CASCADE,
    CONSTRAINT fk_sc_items_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT uq_stock_count_item UNIQUE (stock_count_id, product_id) DEFERRABLE INITIALLY DEFERRED
);

-- 4. Trigger for region-company matching on stock_counts
CREATE OR REPLACE FUNCTION validate_stock_count()
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
        RAISE EXCEPTION 'Referenced location must belong to the stock count company';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_validate_stock_count
BEFORE INSERT OR UPDATE ON stock_counts
FOR EACH ROW
EXECUTE FUNCTION validate_stock_count();

-- 5. Trigger for overlapping active stock count checks
CREATE OR REPLACE FUNCTION validate_stock_count_item_overlap()
RETURNS TRIGGER AS $$
DECLARE
    v_has_overlap BOOLEAN;
    v_new_wh_id BIGINT;
    v_new_store_id BIGINT;
BEGIN
    -- Get location of the new stock count
    SELECT warehouse_id, store_id INTO v_new_wh_id, v_new_store_id
    FROM stock_counts
    WHERE id = NEW.stock_count_id;

    -- Check if another active count exists at the same location with the same product
    SELECT EXISTS (
        SELECT 1
        FROM stock_count_items sci
        JOIN stock_counts sc ON sci.stock_count_id = sc.id
        WHERE sc.id <> NEW.stock_count_id
          AND sc.status IN ('DRAFT', 'ASSIGNED', 'IN_PROGRESS', 'SUBMITTED', 'REJECTED', 'APPROVED')
          AND sci.product_id = NEW.product_id
          AND (
              (sc.warehouse_id IS NOT NULL AND sc.warehouse_id = v_new_wh_id)
              OR
              (sc.store_id IS NOT NULL AND sc.store_id = v_new_store_id)
          )
    ) INTO v_has_overlap;

    IF v_has_overlap THEN
        RAISE EXCEPTION 'An active stock count already exists for product % at the same location', NEW.product_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_validate_stock_count_item_overlap
BEFORE INSERT OR UPDATE ON stock_count_items
FOR EACH ROW
EXECUTE FUNCTION validate_stock_count_item_overlap();

-- 6. Indexes for performance optimization
CREATE INDEX idx_stock_counts_company ON stock_counts(company_id);
CREATE INDEX idx_stock_counts_status ON stock_counts(status);
CREATE INDEX idx_stock_counts_wh ON stock_counts(warehouse_id);
CREATE INDEX idx_stock_counts_store ON stock_counts(store_id);
CREATE INDEX idx_stock_counts_client_ref ON stock_counts(client_reference_id);
CREATE INDEX idx_stock_counts_assigned ON stock_counts(assigned_to);
CREATE INDEX idx_stock_count_items_sc ON stock_count_items(stock_count_id);
CREATE INDEX idx_stock_count_items_product ON stock_count_items(product_id);
