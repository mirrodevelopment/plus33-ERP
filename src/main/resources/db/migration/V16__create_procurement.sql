-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 16
-- File              : V16__create_procurement.sql
-- Operation Type    : Schema Creation
-- Purpose           : create procurement
--
-- Tables Created    : suppliers, purchase_requests, purchase_request_items, purchase_orders, purchase_order_items
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : idx_purchase_requests_user, idx_purchase_requests_warehouse, idx_purchase_requests_store, idx_purchase_request_items_request, idx_purchase_request_items_product, idx_purchase_orders_supplier, idx_purchase_orders_request, idx_purchase_orders_user, idx_purchase_order_items_order, idx_purchase_order_items_product
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V16__create_procurement.sql
-- PLUS33 ERP — Procurement Management Tables
-- ============================================================

CREATE TABLE suppliers (
    id BIGSERIAL PRIMARY KEY,

    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,

    contact_person VARCHAR(150),
    email VARCHAR(150),
    phone VARCHAR(30),

    address TEXT,

    tax_number VARCHAR(100),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE purchase_requests (
    id BIGSERIAL PRIMARY KEY,

    request_number VARCHAR(50) NOT NULL UNIQUE,

    requested_by BIGINT NOT NULL,

    warehouse_id BIGINT,
    store_id BIGINT,

    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

    requested_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_purchase_requests_user
        FOREIGN KEY (requested_by)
        REFERENCES users(id),

    CONSTRAINT fk_purchase_requests_warehouse
        FOREIGN KEY (warehouse_id)
        REFERENCES warehouses(id),

    CONSTRAINT fk_purchase_requests_store
        FOREIGN KEY (store_id)
        REFERENCES stores(id),

    CONSTRAINT chk_purchase_requests_location
        CHECK (
            (warehouse_id IS NOT NULL AND store_id IS NULL)
            OR
            (warehouse_id IS NULL AND store_id IS NOT NULL)
        )
);

CREATE TABLE purchase_request_items (
    id BIGSERIAL PRIMARY KEY,

    purchase_request_id BIGINT NOT NULL,

    product_id BIGINT NOT NULL,

    quantity DECIMAL(12,2) NOT NULL,

    CONSTRAINT fk_pr_items_request
        FOREIGN KEY (purchase_request_id)
        REFERENCES purchase_requests(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_pr_items_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
);

CREATE TABLE purchase_orders (
    id BIGSERIAL PRIMARY KEY,

    order_number VARCHAR(50) NOT NULL UNIQUE,

    supplier_id BIGINT NOT NULL,

    purchase_request_id BIGINT,

    ordered_by BIGINT NOT NULL,

    expected_delivery_date DATE,

    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_purchase_orders_supplier
        FOREIGN KEY (supplier_id)
        REFERENCES suppliers(id),

    CONSTRAINT fk_purchase_orders_request
        FOREIGN KEY (purchase_request_id)
        REFERENCES purchase_requests(id),

    CONSTRAINT fk_purchase_orders_user
        FOREIGN KEY (ordered_by)
        REFERENCES users(id)
);

CREATE TABLE purchase_order_items (
    id BIGSERIAL PRIMARY KEY,

    purchase_order_id BIGINT NOT NULL,

    product_id BIGINT NOT NULL,

    ordered_quantity DECIMAL(12,2) NOT NULL,

    unit_price DECIMAL(12,2) NOT NULL,

    received_quantity DECIMAL(12,2) NOT NULL DEFAULT 0,

    CONSTRAINT fk_po_items_order
        FOREIGN KEY (purchase_order_id)
        REFERENCES purchase_orders(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_po_items_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
);

CREATE INDEX idx_purchase_requests_user ON purchase_requests(requested_by);
CREATE INDEX idx_purchase_requests_warehouse ON purchase_requests(warehouse_id);
CREATE INDEX idx_purchase_requests_store ON purchase_requests(store_id);
CREATE INDEX idx_purchase_request_items_request ON purchase_request_items(purchase_request_id);
CREATE INDEX idx_purchase_request_items_product ON purchase_request_items(product_id);
CREATE INDEX idx_purchase_orders_supplier ON purchase_orders(supplier_id);
CREATE INDEX idx_purchase_orders_request ON purchase_orders(purchase_request_id);
CREATE INDEX idx_purchase_orders_user ON purchase_orders(ordered_by);
CREATE INDEX idx_purchase_order_items_order ON purchase_order_items(purchase_order_id);
CREATE INDEX idx_purchase_order_items_product ON purchase_order_items(product_id);
