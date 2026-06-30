-- V93: Genealogy & 3PL Multi-Tenancy Tables
ALTER TABLE location_stock ADD COLUMN IF NOT EXISTS owner_company_id BIGINT;
ALTER TABLE inventory_movements ADD COLUMN IF NOT EXISTS owner_company_id BIGINT;

CREATE TABLE IF NOT EXISTS lot_genealogy (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    parent_lot_number VARCHAR(50) NOT NULL,
    child_lot_number VARCHAR(50) NOT NULL,
    product_id BIGINT NOT NULL,
    production_order_id BIGINT,
    shipment_id BIGINT,
    customer_return_id BIGINT,
    transformation_type VARCHAR(30) NOT NULL DEFAULT 'SPLIT',
    split_ratio NUMERIC(18, 6),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS serial_genealogy (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    parent_serial_number VARCHAR(100),
    child_serial_number VARCHAR(100) NOT NULL,
    product_id BIGINT NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    location_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
