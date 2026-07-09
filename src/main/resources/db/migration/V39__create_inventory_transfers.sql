-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 39
-- File              : V39__create_inventory_transfers.sql
-- Operation Type    : Schema Creation
-- Purpose           : create inventory transfers
--
-- Tables Created    : inventory_transfers, inventory_transfer_items
-- Tables Altered    : inventory_stock, inventory_stock
-- Seed Data For     : N/A
-- Indexes           : idx_transfers_company, idx_transfers_status, idx_transfers_source_wh, idx_transfers_source_store, idx_transfers_dest_wh, idx_transfers_dest_store, idx_transfers_client_ref, idx_transfer_items_transfer
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V39__create_inventory_transfers.sql
-- PLUS33 ERP — Inventory Transfer schema and triggers
-- ============================================================

-- 1. Alter inventory_stock table to support reservations
ALTER TABLE inventory_stock ADD COLUMN reserved_quantity DECIMAL(12,2) NOT NULL DEFAULT 0.00;

ALTER TABLE inventory_stock ADD CONSTRAINT chk_inventory_stock_reserved
    CHECK (reserved_quantity >= 0 AND reserved_quantity <= quantity);

-- 2. Create sequence for inventory transfer numbers
CREATE SEQUENCE IF NOT EXISTS inventory_transfer_seq START WITH 1 INCREMENT BY 1;

-- 3. Create inventory_transfers table
CREATE TABLE inventory_transfers (
    id BIGSERIAL PRIMARY KEY,
    transfer_number VARCHAR(50) NOT NULL UNIQUE,
    company_id BIGINT NOT NULL,
    source_warehouse_id BIGINT,
    source_store_id BIGINT,
    dest_warehouse_id BIGINT,
    dest_store_id BIGINT,
    status VARCHAR(30) NOT NULL,
    client_reference_id UUID NOT NULL UNIQUE,
    remarks VARCHAR(255),
    created_by BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    submitted_by BIGINT,
    submitted_at TIMESTAMP,
    approved_by BIGINT,
    approved_at TIMESTAMP,
    dispatched_by BIGINT,
    dispatched_at TIMESTAMP,
    received_by BIGINT,
    received_at TIMESTAMP,
    cancelled_by BIGINT,
    cancelled_at TIMESTAMP,
    cancellation_reason VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_transfers_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_transfers_source_wh FOREIGN KEY (source_warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_transfers_source_store FOREIGN KEY (source_store_id) REFERENCES stores(id),
    CONSTRAINT fk_transfers_dest_wh FOREIGN KEY (dest_warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_transfers_dest_store FOREIGN KEY (dest_store_id) REFERENCES stores(id),
    CONSTRAINT fk_transfers_created FOREIGN KEY (created_by) REFERENCES users(id),
    CONSTRAINT fk_transfers_submitted FOREIGN KEY (submitted_by) REFERENCES users(id),
    CONSTRAINT fk_transfers_approved FOREIGN KEY (approved_by) REFERENCES users(id),
    CONSTRAINT fk_transfers_dispatched FOREIGN KEY (dispatched_by) REFERENCES users(id),
    CONSTRAINT fk_transfers_received FOREIGN KEY (received_by) REFERENCES users(id),
    CONSTRAINT fk_transfers_cancelled FOREIGN KEY (cancelled_by) REFERENCES users(id),

    CONSTRAINT chk_transfers_status CHECK (
        status IN ('DRAFT', 'SUBMITTED', 'APPROVED', 'IN_TRANSIT', 'RECEIVED', 'REJECTED', 'CANCELLED', 'CLOSED')
    ),
    -- XOR constraints for source location (must be either warehouse or store, not both)
    CONSTRAINT chk_transfers_source CHECK (
        (source_warehouse_id IS NOT NULL AND source_store_id IS NULL)
        OR
        (source_warehouse_id IS NULL AND source_store_id IS NOT NULL)
    ),
    -- XOR constraints for dest location (must be either warehouse or store, not both)
    CONSTRAINT chk_transfers_dest CHECK (
        (dest_warehouse_id IS NOT NULL AND dest_store_id IS NULL)
        OR
        (dest_warehouse_id IS NULL AND dest_store_id IS NOT NULL)
    )
);

-- 4. Create inventory_transfer_items table
CREATE TABLE inventory_transfer_items (
    id BIGSERIAL PRIMARY KEY,
    transfer_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity DECIMAL(12,2) NOT NULL,
    received_quantity DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    remaining_quantity DECIMAL(12,2) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_items_transfer FOREIGN KEY (transfer_id) REFERENCES inventory_transfers(id) ON DELETE CASCADE,
    CONSTRAINT fk_items_product FOREIGN KEY (product_id) REFERENCES products(id),

    CONSTRAINT chk_items_qty CHECK (quantity > 0),
    CONSTRAINT chk_items_received CHECK (received_quantity >= 0),
    CONSTRAINT chk_items_remaining CHECK (remaining_quantity >= 0),
    CONSTRAINT chk_items_received_limit CHECK (received_quantity <= quantity)
);

-- 5. Trigger for database-level validation
CREATE OR REPLACE FUNCTION validate_inventory_transfer()
RETURNS TRIGGER AS $$
DECLARE
    source_company_id BIGINT;
    dest_company_id BIGINT;
BEGIN
    -- 1. Source and Destination inequality
    IF NEW.source_warehouse_id IS NOT NULL AND NEW.dest_warehouse_id IS NOT NULL AND NEW.source_warehouse_id = NEW.dest_warehouse_id THEN
        RAISE EXCEPTION 'Source and destination warehouses cannot be the same';
    END IF;
    IF NEW.source_store_id IS NOT NULL AND NEW.dest_store_id IS NOT NULL AND NEW.source_store_id = NEW.dest_store_id THEN
        RAISE EXCEPTION 'Source and destination stores cannot be the same';
    END IF;

    -- 2. Store to Warehouse transfers not allowed
    IF NEW.source_store_id IS NOT NULL AND NEW.dest_warehouse_id IS NOT NULL THEN
        RAISE EXCEPTION 'Store-to-Warehouse transfers are prohibited';
    END IF;

    -- 3. Source and Destination company match
    -- Get source company ID
    IF NEW.source_warehouse_id IS NOT NULL THEN
        SELECT r.company_id INTO source_company_id
        FROM warehouses w JOIN regions r ON w.region_id = r.id
        WHERE w.id = NEW.source_warehouse_id;
    ELSE
        SELECT r.company_id INTO source_company_id
        FROM stores s JOIN regions r ON s.region_id = r.id
        WHERE s.id = NEW.source_store_id;
    END IF;

    -- Get destination company ID
    IF NEW.dest_warehouse_id IS NOT NULL THEN
        SELECT r.company_id INTO dest_company_id
        FROM warehouses w JOIN regions r ON w.region_id = r.id
        WHERE w.id = NEW.dest_warehouse_id;
    ELSE
        SELECT r.company_id INTO dest_company_id
        FROM stores s JOIN regions r ON s.region_id = r.id
        WHERE s.id = NEW.dest_store_id;
    END IF;

    IF source_company_id IS NULL OR dest_company_id IS NULL THEN
        RAISE EXCEPTION 'Invalid location reference';
    END IF;

    IF source_company_id <> NEW.company_id OR dest_company_id <> NEW.company_id THEN
        RAISE EXCEPTION 'Source and destination locations must belong to the transfer company';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_validate_inventory_transfer
BEFORE INSERT OR UPDATE ON inventory_transfers
FOR EACH ROW
EXECUTE FUNCTION validate_inventory_transfer();

-- 6. Create indexes for performance optimization
CREATE INDEX idx_transfers_company ON inventory_transfers(company_id);
CREATE INDEX idx_transfers_status ON inventory_transfers(status);
CREATE INDEX idx_transfers_source_wh ON inventory_transfers(source_warehouse_id);
CREATE INDEX idx_transfers_source_store ON inventory_transfers(source_store_id);
CREATE INDEX idx_transfers_dest_wh ON inventory_transfers(dest_warehouse_id);
CREATE INDEX idx_transfers_dest_store ON inventory_transfers(dest_store_id);
CREATE INDEX idx_transfers_client_ref ON inventory_transfers(client_reference_id);
CREATE INDEX idx_transfer_items_transfer ON inventory_transfer_items(transfer_id);
