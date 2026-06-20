-- ============================================================
-- V15__create_inventory.sql
-- PLUS33 ERP — Inventory Management Tables & Seed Data
-- ============================================================

CREATE TABLE product_categories (
    id BIGSERIAL PRIMARY KEY,

    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,

    parent_id BIGINT,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_product_categories_parent
        FOREIGN KEY (parent_id)
        REFERENCES product_categories(id)
);

CREATE TABLE units_of_measure (
    id BIGSERIAL PRIMARY KEY,

    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,

    sku VARCHAR(100) NOT NULL UNIQUE,

    name VARCHAR(150) NOT NULL,

    category_id BIGINT NOT NULL,

    unit_id BIGINT NOT NULL,

    product_type VARCHAR(50) NOT NULL,

    reorder_level DECIMAL(12,2) DEFAULT 0,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_products_category
        FOREIGN KEY (category_id)
        REFERENCES product_categories(id),

    CONSTRAINT fk_products_unit
        FOREIGN KEY (unit_id)
        REFERENCES units_of_measure(id)
);

CREATE TABLE inventory_stock (
    id BIGSERIAL PRIMARY KEY,

    product_id BIGINT NOT NULL,

    warehouse_id BIGINT,

    store_id BIGINT,

    quantity DECIMAL(12,2) NOT NULL DEFAULT 0,

    CONSTRAINT fk_inventory_stock_product
        FOREIGN KEY (product_id)
        REFERENCES products(id),

    CONSTRAINT fk_inventory_stock_warehouse
        FOREIGN KEY (warehouse_id)
        REFERENCES warehouses(id),

    CONSTRAINT fk_inventory_stock_store
        FOREIGN KEY (store_id)
        REFERENCES stores(id),

    CONSTRAINT chk_inventory_stock_location
        CHECK (
            (warehouse_id IS NOT NULL AND store_id IS NULL)
            OR
            (warehouse_id IS NULL AND store_id IS NOT NULL)
        )
);

CREATE TABLE stock_movements (
    id BIGSERIAL PRIMARY KEY,

    product_id BIGINT NOT NULL,

    warehouse_id BIGINT,

    store_id BIGINT,

    movement_type VARCHAR(50) NOT NULL,

    quantity DECIMAL(12,2) NOT NULL,

    reference_no VARCHAR(100),

    movement_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    created_by BIGINT,

    CONSTRAINT fk_stock_movements_product
        FOREIGN KEY (product_id)
        REFERENCES products(id),

    CONSTRAINT fk_stock_movements_warehouse
        FOREIGN KEY (warehouse_id)
        REFERENCES warehouses(id),

    CONSTRAINT fk_stock_movements_store
        FOREIGN KEY (store_id)
        REFERENCES stores(id),

    CONSTRAINT fk_stock_movements_user
        FOREIGN KEY (created_by)
        REFERENCES users(id),

    CONSTRAINT chk_stock_movements_location
        CHECK (
            (warehouse_id IS NOT NULL AND store_id IS NULL)
            OR
            (warehouse_id IS NULL AND store_id IS NOT NULL)
            OR
            (warehouse_id IS NULL AND store_id IS NULL)
        )
);

CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_unit ON products(unit_id);
CREATE INDEX idx_inventory_stock_product ON inventory_stock(product_id);
CREATE INDEX idx_inventory_stock_warehouse ON inventory_stock(warehouse_id);
CREATE INDEX idx_inventory_stock_store ON inventory_stock(store_id);
CREATE INDEX idx_stock_movements_product ON stock_movements(product_id);
CREATE INDEX idx_stock_movements_warehouse ON stock_movements(warehouse_id);
CREATE INDEX idx_stock_movements_store ON stock_movements(store_id);

-- Seed recommended leave types and units of measure
INSERT INTO units_of_measure (code, name) VALUES
('KG', 'Kilogram'),
('LITER', 'Liter'),
('PIECE', 'Piece'),
('BOX', 'Box'),
('PACK', 'Pack');

INSERT INTO product_categories (code, name, parent_id, active) VALUES
('COFFEE_BEANS', 'Coffee Beans', NULL, TRUE),
('MILK_DAIRY', 'Milk & Dairy', NULL, TRUE),
('SYRUPS', 'Syrups', NULL, TRUE),
('PACKAGING', 'Packaging', NULL, TRUE),
('DESSERTS', 'Desserts', NULL, TRUE),
('MERCHANDISE', 'Merchandise', NULL, TRUE);

INSERT INTO products (sku, name, category_id, unit_id, product_type, reorder_level, active)
VALUES
('RAW-ARA-001', 'Arabica Beans', 
  (SELECT id FROM product_categories WHERE code = 'COFFEE_BEANS'),
  (SELECT id FROM units_of_measure WHERE code = 'KG'),
  'RAW_MATERIAL', 10.00, TRUE),

('RAW-MLK-001', 'Milk', 
  (SELECT id FROM product_categories WHERE code = 'MILK_DAIRY'),
  (SELECT id FROM units_of_measure WHERE code = 'LITER'),
  'RAW_MATERIAL', 20.00, TRUE),

('RAW-CHCS-001', 'Chocolate Syrup', 
  (SELECT id FROM product_categories WHERE code = 'SYRUPS'),
  (SELECT id FROM units_of_measure WHERE code = 'LITER'),
  'RAW_MATERIAL', 5.00, TRUE),

('PKG-CUP-001', 'Paper Cups', 
  (SELECT id FROM product_categories WHERE code = 'PACKAGING'),
  (SELECT id FROM units_of_measure WHERE code = 'PIECE'),
  'PACKAGING', 500.00, TRUE),

('FNS-CRO-001', 'Croissants', 
  (SELECT id FROM product_categories WHERE code = 'DESSERTS'),
  (SELECT id FROM units_of_measure WHERE code = 'PIECE'),
  'FINISHED_GOOD', 30.00, TRUE);
