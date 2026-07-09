-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 48
-- File              : V48__create_inventory_traceability.sql
-- Operation Type    : Schema Creation
-- Purpose           : create inventory traceability
--
-- Tables Created    : inventory_lots, inventory_serials, inventory_recalls, inventory_trace_events
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : idx_inv_lots_product, idx_inv_serials_product, idx_inv_serials_lot, idx_inv_trace_product, idx_inv_trace_lot, idx_inv_trace_serial, idx_trace_reference, idx_trace_created_at, idx_lot_expiry, uq_active_recall_lot, uq_active_recall_serial
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V48__create_inventory_traceability.sql
-- PLUS33 ERP — Inventory Traceability Schema
-- ============================================================

-- 1. inventory_lots table
CREATE TABLE inventory_lots (
    id                 BIGSERIAL PRIMARY KEY,
    company_id         BIGINT NOT NULL,
    product_id         BIGINT NOT NULL,
    lot_number         VARCHAR(100) NOT NULL,
    expiry_date        DATE NOT NULL,
    manufactured_date  DATE,
    status             VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    version            BIGINT NOT NULL DEFAULT 0,
    created_at         TIMESTAMP NOT NULL DEFAULT now(),
    updated_at         TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_inv_lots_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_inv_lots_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT uq_lot_number UNIQUE (company_id, product_id, lot_number),
    CONSTRAINT chk_lot_dates CHECK (manufactured_date IS NULL OR expiry_date >= manufactured_date),
    CONSTRAINT chk_lot_status CHECK (status IN ('ACTIVE', 'QUARANTINED', 'RECALLED', 'EXPIRED'))
);

-- 2. inventory_serials table
CREATE TABLE inventory_serials (
    id                 BIGSERIAL PRIMARY KEY,
    company_id         BIGINT NOT NULL,
    product_id         BIGINT NOT NULL,
    lot_id             BIGINT,
    serial_number      VARCHAR(100) NOT NULL,
    warehouse_id       BIGINT,
    store_id           BIGINT,
    status             VARCHAR(50) NOT NULL DEFAULT 'IN_STOCK',
    version            BIGINT NOT NULL DEFAULT 0,
    created_at         TIMESTAMP NOT NULL DEFAULT now(),
    updated_at         TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_inv_serials_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_inv_serials_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_inv_serials_lot     FOREIGN KEY (lot_id)     REFERENCES inventory_lots(id),
    CONSTRAINT fk_inv_serials_wh      FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_inv_serials_store   FOREIGN KEY (store_id)     REFERENCES stores(id),

    CONSTRAINT uq_serial_number UNIQUE (company_id, product_id, serial_number),
    CONSTRAINT chk_serial_location CHECK (
        (warehouse_id IS NULL AND store_id IS NULL) OR
        (warehouse_id IS NOT NULL AND store_id IS NULL) OR
        (store_id IS NOT NULL AND warehouse_id IS NULL)
    ),
    CONSTRAINT chk_serial_status CHECK (status IN ('IN_STOCK', 'SOLD', 'TRANSIT', 'RECALLED', 'QUARANTINED'))
);

-- 3. inventory_recalls table
CREATE TABLE inventory_recalls (
    id                      BIGSERIAL PRIMARY KEY,
    company_id              BIGINT NOT NULL,
    product_id              BIGINT NOT NULL,
    lot_id                  BIGINT,
    serial_id               BIGINT,
    recall_number           VARCHAR(50) NOT NULL UNIQUE,
    recall_reason           VARCHAR(255) NOT NULL,
    recall_reference_number VARCHAR(100),
    status                  VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    recalled_by_id          BIGINT NOT NULL,
    recalled_at             TIMESTAMP NOT NULL DEFAULT now(),
    created_at              TIMESTAMP NOT NULL DEFAULT now(),
    updated_at              TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_inv_recalls_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_inv_recalls_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_inv_recalls_lot     FOREIGN KEY (lot_id)     REFERENCES inventory_lots(id),
    CONSTRAINT fk_inv_recalls_serial  FOREIGN KEY (serial_id)  REFERENCES inventory_serials(id),
    CONSTRAINT fk_inv_recalls_user    FOREIGN KEY (recalled_by_id) REFERENCES users(id),

    CONSTRAINT chk_recall_entity CHECK (
        (lot_id IS NOT NULL AND serial_id IS NULL) OR
        (serial_id IS NOT NULL AND lot_id IS NULL) OR
        (lot_id IS NULL AND serial_id IS NULL)
    ),
    CONSTRAINT chk_recall_status CHECK (status IN ('ACTIVE', 'CLOSED', 'CANCELLED'))
);

-- 4. inventory_trace_events table
CREATE TABLE inventory_trace_events (
    id                BIGSERIAL PRIMARY KEY,
    company_id        BIGINT NOT NULL,
    product_id        BIGINT NOT NULL,
    lot_id            BIGINT,
    serial_id         BIGINT,
    warehouse_id      BIGINT,
    store_id          BIGINT,
    event_type        VARCHAR(50) NOT NULL,
    quantity          NUMERIC(12,2) NOT NULL,
    reference_type    VARCHAR(50) NOT NULL,
    reference_id      BIGINT NOT NULL,
    reference_number  VARCHAR(50) NOT NULL,
    notes             TEXT,
    created_by        BIGINT,
    created_at        TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_inv_trace_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_inv_trace_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_inv_trace_lot     FOREIGN KEY (lot_id)     REFERENCES inventory_lots(id),
    CONSTRAINT fk_inv_trace_serial  FOREIGN KEY (serial_id)  REFERENCES inventory_serials(id),
    CONSTRAINT fk_inv_trace_wh      FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_inv_trace_store   FOREIGN KEY (store_id)     REFERENCES stores(id),
    CONSTRAINT fk_inv_trace_user    FOREIGN KEY (created_by)   REFERENCES users(id),

    CONSTRAINT chk_trace_location CHECK (
        (warehouse_id IS NULL AND store_id IS NULL) OR
        (warehouse_id IS NOT NULL AND store_id IS NULL) OR
        (store_id IS NOT NULL AND warehouse_id IS NULL)
    ),
    CONSTRAINT chk_trace_event_type CHECK (
        event_type IN ('RECEIPT', 'TRANSFER_OUT', 'TRANSFER_IN', 'ADJUSTMENT', 'COUNT_VARIANCE', 'RECALL', 'SALE', 'EXPIRED')
    ),
    CONSTRAINT chk_trace_ref_type CHECK (
        reference_type IN ('GOODS_RECEIPT', 'INVENTORY_TRANSFER', 'INVENTORY_ADJUSTMENT', 'STOCK_COUNT', 'SALES_ORDER', 'INVENTORY_RECALL')
    )
);

-- Indexes
CREATE INDEX idx_inv_lots_product     ON inventory_lots(product_id);
CREATE INDEX idx_inv_serials_product  ON inventory_serials(product_id);
CREATE INDEX idx_inv_serials_lot      ON inventory_serials(lot_id);
CREATE INDEX idx_inv_trace_product    ON inventory_trace_events(product_id);
CREATE INDEX idx_inv_trace_lot        ON inventory_trace_events(lot_id);
CREATE INDEX idx_inv_trace_serial     ON inventory_trace_events(serial_id);
CREATE INDEX idx_trace_reference      ON inventory_trace_events(reference_type, reference_id);
CREATE INDEX idx_trace_created_at     ON inventory_trace_events(created_at);
CREATE INDEX idx_lot_expiry           ON inventory_lots(expiry_date, status);

-- Unique index to prevent duplicate active recalls
CREATE UNIQUE INDEX uq_active_recall_lot
ON inventory_recalls(lot_id)
WHERE status = 'ACTIVE';

CREATE UNIQUE INDEX uq_active_recall_serial
ON inventory_recalls(serial_id)
WHERE status = 'ACTIVE';

-- Trigger company validation for warehouse/store mapping
CREATE OR REPLACE FUNCTION validate_traceability_location_company()
RETURNS TRIGGER AS $$
DECLARE
    loc_company_id BIGINT;
BEGIN
    IF NEW.warehouse_id IS NOT NULL THEN
        SELECT r.company_id INTO loc_company_id FROM warehouses w
        JOIN regions r ON w.region_id = r.id WHERE w.id = NEW.warehouse_id;
        IF loc_company_id IS NULL OR loc_company_id <> NEW.company_id THEN
            RAISE EXCEPTION 'Warehouse company mismatch on traceability item';
        END IF;
    ELSIF NEW.store_id IS NOT NULL THEN
        SELECT r.company_id INTO loc_company_id FROM stores s
        JOIN regions r ON s.region_id = r.id WHERE s.id = NEW.store_id;
        IF loc_company_id IS NULL OR loc_company_id <> NEW.company_id THEN
            RAISE EXCEPTION 'Store company mismatch on traceability item';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_inv_serials_company
    BEFORE INSERT OR UPDATE ON inventory_serials
    FOR EACH ROW EXECUTE FUNCTION validate_traceability_location_company();

CREATE TRIGGER trg_validate_inv_trace_company
    BEFORE INSERT OR UPDATE ON inventory_trace_events
    FOR EACH ROW EXECUTE FUNCTION validate_traceability_location_company();

-- Trigger company lot/serial match on recall
CREATE OR REPLACE FUNCTION validate_recall_company()
RETURNS TRIGGER AS $$
DECLARE
    lot_comp BIGINT;
    serial_comp BIGINT;
BEGIN
    IF NEW.lot_id IS NOT NULL THEN
        SELECT company_id INTO lot_comp FROM inventory_lots WHERE id = NEW.lot_id;
        IF lot_comp <> NEW.company_id THEN
            RAISE EXCEPTION 'Recall company must match target lot company';
        END IF;
    END IF;
    IF NEW.serial_id IS NOT NULL THEN
        SELECT company_id INTO serial_comp FROM inventory_serials WHERE id = NEW.serial_id;
        IF serial_comp <> NEW.company_id THEN
            RAISE EXCEPTION 'Recall company must match target serial company';
        END If;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_recall_company
    BEFORE INSERT OR UPDATE ON inventory_recalls
    FOR EACH ROW EXECUTE FUNCTION validate_recall_company();

-- Updated_at triggers
CREATE OR REPLACE FUNCTION update_traceability_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_inventory_lots_updated_at BEFORE UPDATE ON inventory_lots FOR EACH ROW EXECUTE FUNCTION update_traceability_updated_at();
CREATE TRIGGER trg_inventory_serials_updated_at BEFORE UPDATE ON inventory_serials FOR EACH ROW EXECUTE FUNCTION update_traceability_updated_at();
CREATE TRIGGER trg_inventory_recalls_updated_at BEFORE UPDATE ON inventory_recalls FOR EACH ROW EXECUTE FUNCTION update_traceability_updated_at();

-- Sequence for recall numbers
CREATE SEQUENCE inventory_recall_seq START WITH 1 INCREMENT BY 1;
