-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 17
-- File              : V17__create_warehouse_operations.sql
-- Operation Type    : Schema Creation
-- Purpose           : create warehouse operations
--
-- Tables Created    : stock_transfers, stock_transfer_items, stock_adjustments
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : idx_stock_transfers_src_wh, idx_stock_transfers_src_store, idx_stock_transfers_dest_wh, idx_stock_transfers_dest_store, idx_stock_transfer_items_transfer, idx_stock_transfer_items_product, idx_stock_adjustments_wh, idx_stock_adjustments_store, idx_stock_adjustments_product
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V17__create_warehouse_operations.sql
-- PLUS33 ERP — Warehouse Operations Tables
-- ============================================================

CREATE TABLE stock_transfers (
    id BIGSERIAL PRIMARY KEY,

    transfer_number VARCHAR(50) NOT NULL UNIQUE,

    source_warehouse_id BIGINT,
    source_store_id BIGINT,

    destination_warehouse_id BIGINT,
    destination_store_id BIGINT,

    requested_by BIGINT NOT NULL,

    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

    requested_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    approved_by BIGINT,
    approved_at TIMESTAMP,

    completed_at TIMESTAMP,

    CONSTRAINT fk_stock_transfers_requested_by
        FOREIGN KEY (requested_by)
        REFERENCES users(id),

    CONSTRAINT fk_stock_transfers_approved_by
        FOREIGN KEY (approved_by)
        REFERENCES users(id),

    CONSTRAINT fk_stock_transfers_src_wh
        FOREIGN KEY (source_warehouse_id)
        REFERENCES warehouses(id),

    CONSTRAINT fk_stock_transfers_src_store
        FOREIGN KEY (source_store_id)
        REFERENCES stores(id),

    CONSTRAINT fk_stock_transfers_dest_wh
        FOREIGN KEY (destination_warehouse_id)
        REFERENCES warehouses(id),

    CONSTRAINT fk_stock_transfers_dest_store
        FOREIGN KEY (destination_store_id)
        REFERENCES stores(id),

    CONSTRAINT chk_stock_transfers_source
        CHECK (
            (source_warehouse_id IS NOT NULL AND source_store_id IS NULL)
            OR
            (source_warehouse_id IS NULL AND source_store_id IS NOT NULL)
        ),

    CONSTRAINT chk_stock_transfers_destination
        CHECK (
            (destination_warehouse_id IS NOT NULL AND destination_store_id IS NULL)
            OR
            (destination_warehouse_id IS NULL AND destination_store_id IS NOT NULL)
        )
);

CREATE TABLE stock_transfer_items (
    id BIGSERIAL PRIMARY KEY,

    stock_transfer_id BIGINT NOT NULL,

    product_id BIGINT NOT NULL,

    requested_quantity DECIMAL(12,2) NOT NULL,

    transferred_quantity DECIMAL(12,2) DEFAULT 0,

    CONSTRAINT fk_transfer_items_transfer
        FOREIGN KEY (stock_transfer_id)
        REFERENCES stock_transfers(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_transfer_items_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
);

CREATE TABLE stock_adjustments (
    id BIGSERIAL PRIMARY KEY,

    adjustment_number VARCHAR(50) NOT NULL UNIQUE,

    warehouse_id BIGINT,
    store_id BIGINT,

    product_id BIGINT NOT NULL,

    previous_quantity DECIMAL(12,2) NOT NULL,

    adjusted_quantity DECIMAL(12,2) NOT NULL,

    reason VARCHAR(255),

    approved_by BIGINT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_stock_adjustments_product
        FOREIGN KEY (product_id)
        REFERENCES products(id),

    CONSTRAINT fk_stock_adjustments_approved_by
        FOREIGN KEY (approved_by)
        REFERENCES users(id),

    CONSTRAINT fk_stock_adjustments_wh
        FOREIGN KEY (warehouse_id)
        REFERENCES warehouses(id),

    CONSTRAINT fk_stock_adjustments_store
        FOREIGN KEY (store_id)
        REFERENCES stores(id),

    CONSTRAINT chk_stock_adjustments_location
        CHECK (
            (warehouse_id IS NOT NULL AND store_id IS NULL)
            OR
            (warehouse_id IS NULL AND store_id IS NOT NULL)
        )
);

CREATE INDEX idx_stock_transfers_src_wh ON stock_transfers(source_warehouse_id);
CREATE INDEX idx_stock_transfers_src_store ON stock_transfers(source_store_id);
CREATE INDEX idx_stock_transfers_dest_wh ON stock_transfers(destination_warehouse_id);
CREATE INDEX idx_stock_transfers_dest_store ON stock_transfers(destination_store_id);
CREATE INDEX idx_stock_transfer_items_transfer ON stock_transfer_items(stock_transfer_id);
CREATE INDEX idx_stock_transfer_items_product ON stock_transfer_items(product_id);
CREATE INDEX idx_stock_adjustments_wh ON stock_adjustments(warehouse_id);
CREATE INDEX idx_stock_adjustments_store ON stock_adjustments(store_id);
CREATE INDEX idx_stock_adjustments_product ON stock_adjustments(product_id);
