-- ============================================================
-- V7__create_organization_structure.sql
-- PLUS33 ERP — Organization module base schema
-- ============================================================

CREATE TABLE companies (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    legal_name VARCHAR(255),
    country_code VARCHAR(10),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE regions (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    company_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_regions_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
);

CREATE TABLE warehouses (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(30),
    email VARCHAR(150),
    timezone VARCHAR(100),
    opening_date DATE,
    region_id BIGINT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_warehouses_region
        FOREIGN KEY (region_id)
        REFERENCES regions(id)
);

CREATE TABLE stores (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(30),
    email VARCHAR(150),
    timezone VARCHAR(100),
    opening_date DATE,
    region_id BIGINT NOT NULL,
    warehouse_id BIGINT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_stores_region
        FOREIGN KEY (region_id)
        REFERENCES regions(id),

    CONSTRAINT fk_stores_warehouse
        FOREIGN KEY (warehouse_id)
        REFERENCES warehouses(id)
);

CREATE INDEX idx_regions_company ON regions(company_id);
CREATE INDEX idx_warehouses_region ON warehouses(region_id);
CREATE INDEX idx_warehouses_active ON warehouses(active);
CREATE INDEX idx_stores_region ON stores(region_id);
CREATE INDEX idx_stores_warehouse ON stores(warehouse_id);
CREATE INDEX idx_stores_active ON stores(active);
