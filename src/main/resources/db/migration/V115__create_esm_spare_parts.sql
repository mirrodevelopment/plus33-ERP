-- V115: Spare Parts Van Inventory and Consumption
CREATE TABLE IF NOT EXISTS esm_van_stocks (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    van_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity_on_hand NUMERIC(18, 6) NOT NULL DEFAULT 0,
    unit_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS esm_parts_reservations (
    id BIGSERIAL PRIMARY KEY,
    work_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity_reserved NUMERIC(18, 6) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'RESERVED'
);
